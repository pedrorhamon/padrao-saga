package br.com.microservices.orchestrated.orderservice.core.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.microservices.orchestrated.orderservice.core.document.Event;
import br.com.microservices.orchestrated.orderservice.core.repository.EventRepository;
import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */
@Service
@AllArgsConstructor
public class EventService {
	
	private final EventRepository eventRepository;
	
	@Transactional
	public Event save(Event event) {
		return this.eventRepository.save(event);
	}

}
