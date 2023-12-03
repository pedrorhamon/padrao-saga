package br.com.microservices.orchestrated.orderservice.core.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.microservices.orchestrated.orderservice.core.document.Event;
import br.com.microservices.orchestrated.orderservice.core.dto.EventFilters;
import br.com.microservices.orchestrated.orderservice.core.service.EventService;
import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */

@RestController
@AllArgsConstructor
@RequestMapping("/api/event")
public class EventController {
	
	private final EventService eventService;
	
	@PostMapping
	public Event createOrder(EventFilters eventFilters) {
		return this.eventService.findAllFilters(eventFilters);
	}
	
	@GetMapping("all")
	public List<Event> findAll() {
		return this.eventService.findAll();
	}

}
