package br.com.microservices.orchestrated.productvalidationservice.core.service;

import org.springframework.stereotype.Service;

import br.com.microservices.orchestrated.productvalidationservice.core.producer.KafkaProducer;
import br.com.microservices.orchestrated.productvalidationservice.core.repository.ProductRepository;
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
	
	private final JsonUtil jsonUtil;
	
	private final KafkaProducer producer;
	
	private final ProductRepository productRepository;

}
