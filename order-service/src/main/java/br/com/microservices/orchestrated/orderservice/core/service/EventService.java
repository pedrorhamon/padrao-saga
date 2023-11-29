package br.com.microservices.orchestrated.orderservice.core.service;

import org.springframework.stereotype.Service;

import br.com.microservices.orchestrated.orderservice.core.repository.EventRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author pedroRhamon
 */
@Service
@RequiredArgsConstructor
public class EventService {
	
	private final EventRepository eventRepository;

}
