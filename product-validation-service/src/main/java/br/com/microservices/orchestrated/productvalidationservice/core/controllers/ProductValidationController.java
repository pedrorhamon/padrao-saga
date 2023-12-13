package br.com.microservices.orchestrated.productvalidationservice.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import br.com.microservices.orchestrated.productvalidationservice.core.service.ProductValidationService;

/**
 * @author pedroRhamon
 */

@RestController
public class ProductValidationController {
	
	@Autowired
	private ProductValidationService service;

}
