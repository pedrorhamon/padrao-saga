package br.com.microservices.orchestrated.inventoryservice.core.service;

import org.springframework.stereotype.Service;

import br.com.microservices.orchestrated.inventoryservice.config.exception.ValidationException;
import br.com.microservices.orchestrated.inventoryservice.core.dto.Event;
import br.com.microservices.orchestrated.inventoryservice.core.dto.OrderProduct;
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
		} catch (Exception e) {
			log.error("Error trying to update inventory: ", e);
		}
	}

	private void createOrderInventory(Event event) {
		event.getPayload()
		.getProducts()
		.forEach(products -> {
			var inventory = this.findInventoryByProductCode(products.getProduct().getCode());
			var orderInventory = this.createOrderInventory(event, products, inventory);
			
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
		// TODO Auto-generated method stub

	}

	private void changeValidationToFail(Event event) {
	}

}
