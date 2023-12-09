package br.com.microservices.orchestrated.productvalidationservice.core.service;

import org.springframework.stereotype.Service;
import static org.springframework.util.ObjectUtils.isEmpty;

import br.com.microservices.orchestrated.productvalidationservice.config.exception.ValidationException;
import br.com.microservices.orchestrated.productvalidationservice.core.dto.Event;
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
			this.createValidation(event);
			this.handleSuccess(event);
		} catch (Exception e) {
			log.error("Error trying to validation products: ", e);
			this.handleFailCurrentNotExecuted(event, e.getMessage());
		}
		
		this.producer.sendEvent(this.jsonUtil.toJson(event));
	}

	private void handleSuccess(Event event) {
		// TODO Auto-generated method stub
		
	}

	private void createValidation(Event event) {
		// TODO Auto-generated method stub
		
	}

	private void checkCurrentValidation(Event event) {
		this.validateProductsInformed(event);
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
