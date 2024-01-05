package br.com.microservices.orchestrated.inventoryservice.core.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.com.microservices.orchestrated.inventoryservice.config.exception.ValidationException;
import br.com.microservices.orchestrated.inventoryservice.core.dto.Event;
import br.com.microservices.orchestrated.inventoryservice.core.dto.History;
import br.com.microservices.orchestrated.inventoryservice.core.dto.Order;
import br.com.microservices.orchestrated.inventoryservice.core.dto.OrderProduct;
import br.com.microservices.orchestrated.inventoryservice.core.enums.ESagaStatus;
import br.com.microservices.orchestrated.inventoryservice.core.model.Inventory;
import br.com.microservices.orchestrated.inventoryservice.core.model.OrderInventory;
import br.com.microservices.orchestrated.inventoryservice.core.producer.KafkaProducer;
import br.com.microservices.orchestrated.inventoryservice.core.repository.InventoryRepository;
import br.com.microservices.orchestrated.inventoryservice.core.repository.OrderInventoryRepository;
import br.com.microservices.orchestrated.inventoryservice.core.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pedroRhamon
 */
@Slf4j
@Service
@AllArgsConstructor
public class InventoryService {

	private static final String CURRENT_SOURCE = "INVENTORY_SERVICE";

	private final JsonUtil jsonUtil;
	private final KafkaProducer producer;
	private final InventoryRepository inventoryRepository;
	private final OrderInventoryRepository orderInventoryRepository;

	public void updateInventory(Event event) {
		try {
			this.checkCurrentValidation(event);
			this.createOrderInventory(event);
			this.updateInventory(event.getPayload());
			this.handleSuccess(event);
		} catch (Exception e) {
			log.error("Error trying to update inventory: ", e);
			this.handleFailCurrentNotExecuted(event, e.getMessage());
		}
	}
	
	private void updateInventory(Order order) {
		order
		.getProducts()
		.forEach(products -> {
			var inventory = this.findInventoryByProductCode(products.getProduct().getCode());
			this.checkInventory(inventory.getAvailable(), products.getQuantity());
			inventory.setAvailable(inventory.getAvailable() - products.getQuantity());
			this.inventoryRepository.save(inventory);
		});
	}
	
	private void checkInventory(int available, int orderQuantity) {
		if(orderQuantity > available) {
			throw new ValidationException("Inventory is out of stock!");
		}
	}
	
	private void handleSuccess(Event event) {
        event.setStatus(ESagaStatus.SUCCESS);
        event.setSource(CURRENT_SOURCE);
        this.addHistory(event, "Inventory realized successfully!");
    }
	
	private void addHistory(Event event, String message) {
        var history = History
            .builder()
            .source(event.getSource())
            .status(event.getStatus())
            .message(message)
            .createdAt(LocalDateTime.now())
            .build();
        event.addToHistory(history);
    }

	private void createOrderInventory(Event event) {
		event.getPayload()
		.getProducts()
		.forEach(products -> {
			var inventory = this.findInventoryByProductCode(products.getProduct().getCode());
			var orderInventory = this.createOrderInventory(event, products, inventory);
			this.orderInventoryRepository.save(orderInventory);
			
		});
	}
	
	private OrderInventory createOrderInventory(Event event, OrderProduct product, Inventory inventory) {
		return OrderInventory
				.builder()
				.inventory(inventory)
				.oldQuantity(inventory.getAvailable())
				.orderQuantity(product.getQuantity())
				.newQuantity(inventory.getAvailable() - product.getQuantity())
				.orderId(event.getPayload().getId())
				.transactionId(event.getTransactionId())
				.build();
	}
	
	private Inventory findInventoryByProductCode(String productCode) {
		return this.inventoryRepository.findByProductCode(productCode)
				.orElseThrow(()-> new ValidationException("Inventory not found by informed product"));
	}

	private void checkCurrentValidation(Event event) {
		if (this.orderInventoryRepository.existsByOrderIdAndTransactionId(event.getOrderId(), event.getTransactionId())) {
			throw new ValidationException("There's another transactionId for this validation.");
		}
	}

	public void rollbackInventory(Event event) {
		
	}

	private void handleFailCurrentNotExecuted(Event event, String message) {
		event.setStatus(ESagaStatus.ROLLBACK_PENDING);
		event.setSource(CURRENT_SOURCE);
		addHistory(event, "Fail to update inventory: ".concat(message));
	}

}
