package br.com.microservices.orchestrated.orderservice.core.service;

import org.springframework.stereotype.Service;

import br.com.microservices.orchestrated.orderservice.core.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author pedroRhamon
 */
@Service
@RequiredArgsConstructor
public class OrderService {
	
	private final OrderRepository orderRepository;

}
