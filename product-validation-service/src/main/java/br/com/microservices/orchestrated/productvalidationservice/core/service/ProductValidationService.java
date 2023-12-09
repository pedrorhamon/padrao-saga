package br.com.microservices.orchestrated.productvalidationservice.core.service;

import org.springframework.stereotype.Service;
import static org.springframework.util.ObjectUtils.isEmpty;

import java.time.LocalDateTime;

import br.com.microservices.orchestrated.productvalidationservice.config.exception.ValidationException;
import br.com.microservices.orchestrated.productvalidationservice.core.dto.Event;
import br.com.microservices.orchestrated.productvalidationservice.core.dto.History;
import br.com.microservices.orchestrated.productvalidationservice.core.dto.OrderProduct;
import br.com.microservices.orchestrated.productvalidationservice.core.enums.ESagaStatus;
import br.com.microservices.orchestrated.productvalidationservice.core.model.Validation;
import br.com.microservices.orchestrated.productvalidationservice.core.producer.KafkaProducer;
import br.com.microservices.orchestrated.productvalidationservice.core.repository.ProductRepository;
import br.com.microservices.orchestrated.productvalidationservice.core.repository.ValidationRepository;
import br.com.microservices.orchestrated.productvalidationservice.core.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pedroRhamon
 */

@Service
@AllArgsConstructor
@Slf4j
public class ProductValidationService {
	
	private static final String CURRENT_SOURCE = "PRODUCT_VALIDATION_SERVICE";
	
	private final JsonUtil jsonUtil;
	
	private final KafkaProducer producer;
	
	private final ProductRepository productRepository;
	
	private final ValidationRepository validationRepository;
	
	public void validationExistingProducts(Event event) {
		try {
			this.checkCurrentValidation(event);
			this.createValidation(event, true);
			this.handleSuccess(event);
		} catch (Exception e) {
			log.error("Error trying to validation products: ", e);
			this.handleFailCurrentNotExecuted(event, e.getMessage());
		}
		
		this.producer.sendEvent(this.jsonUtil.toJson(event));
	}

	private void handleFailCurrentNotExecuted(Event event, String message) {
		// TODO Auto-generated method stub
		
	}

	private void handleSuccess(Event event) {
		event.setStatus(ESagaStatus.SUCCESS);
		event.setSource(CURRENT_SOURCE);
		this.addHistory(event, "Products are validated successfully!");
	}
	
	private void addHistory(Event event, String message) {
		var history = History.builder()
				.source(event.getSource())
				.status(event.getStatus())
				.message(message)
				.createdAt(LocalDateTime.now())
				.build();
		
		event.addToHistory(history);
	}

	private void createValidation(Event event, boolean success) {
		var validation = Validation.builder()
				.orderId(event.getId())
				.transactionId(event.getPlayload().getId())
				.success(success)
				.build();
		
		this.validationRepository.save(validation);
	}

	private void checkCurrentValidation(Event event) {
		this.validateProductsInformed(event);
		if(this.validationRepository.existsByOrderIdAndTransactionId(event.getOrderId(), event.getTransactionId())) {
			throw new ValidationException("There's another transactionId for this validation");
		}
		
		event.getPlayload().getProducts().forEach(product -> {
			this.validationProductInformed(product);
			this.validateExistingProduct(product.getProduct().getCode());
		});
	}

	private void validationProductInformed(OrderProduct orderProduct) {
		if(isEmpty(orderProduct.getProduct()) || isEmpty(orderProduct.getProduct().getCode())) {
			throw new ValidationException("Product List is empty!");
		}
	}
	
	private void validateExistingProduct(String code) {
		if (!this.productRepository.existsByCode(code)) {
			throw new ValidationException("Product does not exists in database!");
		}
	}
	
	private void validateProductsInformed(Event event) {
		if(isEmpty(event.getPlayload().getId()) || isEmpty(event.getPlayload().getProducts())) {
			throw new ValidationException("Product List is empty!");
		}
		if(isEmpty(event.getPlayload()) || isEmpty(event.getPlayload().getProducts())) {
			throw new ValidationException("OrderID and TransactionID must be informed!");
		}
	}

}
