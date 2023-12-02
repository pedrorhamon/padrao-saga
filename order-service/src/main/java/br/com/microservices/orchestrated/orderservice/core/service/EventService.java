package br.com.microservices.orchestrated.orderservice.core.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.microservices.orchestrated.orderservice.core.document.Event;
import br.com.microservices.orchestrated.orderservice.core.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pedroRhamon
 */
@Slf4j
@Service
@AllArgsConstructor
public class EventService {
	
	private final EventRepository eventRepository;
	
	public void notifyEnding(Event event) {
		event.setOrderId(event.getOrderId());
		event.setCreatedAt(LocalDateTime.now());
		this.save(event);
		log.info("Order {} with saga notified! TransactionID: {}", event.getOrderId(), event.getTransactionId());
	}
	
	@Transactional
	public Event save(Event event) {
		return this.eventRepository.save(event);
	}

}
