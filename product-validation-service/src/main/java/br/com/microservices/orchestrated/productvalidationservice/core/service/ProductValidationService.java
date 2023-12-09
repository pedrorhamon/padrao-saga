package br.com.microservices.orchestrated.productvalidationservice.core.service;

import org.springframework.stereotype.Service;

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
			checkCurrentValidation(event);
			createValidation(event);
			handleSuccess(event);
		} catch (Exception e) {
			log.error("Error trying to validation products: ", e);
		}
		
		this.producer.sendEvent(this.jsonUtil.toJson(event));
	}

}
