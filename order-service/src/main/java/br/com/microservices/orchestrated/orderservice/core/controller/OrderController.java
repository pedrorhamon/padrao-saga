package br.com.microservices.orchestrated.orderservice.core.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.microservices.orchestrated.orderservice.core.document.Order;
import br.com.microservices.orchestrated.orderservice.core.dto.OrderRequest;
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
	
	@PostMapping
	public Order createOrder(@RequestBody OrderRequest orderRequest) {
		return this.orderService.createOrder(orderRequest);
	}

}
