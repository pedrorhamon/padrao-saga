package br.com.microservices.orchestrated.orderservice.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.microservices.orchestrated.orderservice.core.service.OrderService;
import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */

@RestController
@AllArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
	
	private final OrderService orderService;

}
