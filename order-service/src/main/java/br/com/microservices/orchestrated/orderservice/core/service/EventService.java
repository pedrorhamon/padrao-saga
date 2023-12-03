package br.com.microservices.orchestrated.orderservice.core.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.microservices.orchestrated.orderservice.core.document.Event;
import br.com.microservices.orchestrated.orderservice.core.dto.EventFilters;
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
	
	public Event findAllFilters(EventFilters eventFilters) {
		this.validateEmptyFilters(eventFilters);
		if(!isEmpty(eventFilters.getOrderId())) {
			return this.findByOrderId(eventFilters.getOrderId());
		} else {
			return this.findByTransactionId(eventFilters.getTransactionId());
		}
	}
	
	private Event findByOrderId(String orderId) {
		return this.eventRepository
				.findTop1ByOrderIdOrderByCreatedAtDesc(orderId)
				.orElseThrow(()-> new ValidateException("Event not found by orderID"));
	}
	
	private Event findByTransactionId(String transactionId) {
		return this.eventRepository
				.findTop1ByTransactionIdOrderByCreatedAtDesc(transactionId)
				.orElseThrow(()-> new ValidateException("Transaction not found by orderID"));
	}
	
	private void validateEmptyFilters(EventFilters eventFilters) {
		if(isEmpty(eventFilters.getOrderId()) && isEmpty(eventFilters.getTransactionId())) {
			throw new ValidateException("OrderId or TransactionID must be informed");
		}
	}
	
	public List<Event> findAll() {
		return this.eventRepository.findAllByOrderByCreatedAtDesc();
	}

}
