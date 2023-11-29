package br.com.microservices.orchestrated.orderservice.core.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.microservices.orchestrated.orderservice.core.document.Event;
import br.com.microservices.orchestrated.orderservice.core.document.Order;
import br.com.microservices.orchestrated.orderservice.core.dto.OrderRequest;
import br.com.microservices.orchestrated.orderservice.core.producer.SagaProducer;
import br.com.microservices.orchestrated.orderservice.core.repository.EventRepository;
import br.com.microservices.orchestrated.orderservice.core.repository.OrderRepository;
import br.com.microservices.orchestrated.orderservice.core.utils.JsonUtil;
import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */
@Service
@AllArgsConstructor
public class OrderService {
	
	private static final String TRANSACTION_ID_PATTERN = "%s_%s";
	
	private final JsonUtil jsonUtil;
	
	private final SagaProducer sagaProducer;
	
	private final EventService eventService;
	
	private final OrderRepository orderRepository;

	public Order createOrder(OrderRequest orderRequest) {
		var order = Order.builder()
				.products(orderRequest.getProducts())
				.createdAt(LocalDateTime.now())
				.transactionId(String.format(TRANSACTION_ID_PATTERN, Instant.now().toEpochMilli(), UUID.randomUUID()))
				.build();
		
		this.orderRepository.save(order);
		this.sagaProducer.sendEvent(this.jsonUtil.toJson(this.createOrder(orderRequest)));
		return order;
	}
	
	private Event createPayload(Order order) {
		var event = Event.builder()
				.orderId(order.getId())
				.transactionId(order.getTransactionId())
				.playload(order)
				.createdAt(LocalDateTime.now())	
				.build();
		return this.eventService.save(event);
	}
}
