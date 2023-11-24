package br.com.microservices.orchestrated.orderservice.core.utils;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.microservices.orchestrated.orderservice.core.document.Event;
import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */

@AllArgsConstructor
@Component
public class JsonUtil {

	private final ObjectMapper mapper;

	public String toJson(Object obj) {
		try {
			return this.mapper.writeValueAsString(obj);
		} catch (Exception e) {
			return "";
		}
	}

	public Event toEvent(String json) {
		try {
			return this.mapper.readValue(json, Event.class);
		} catch (Exception e) {
			return null;
		}
	}
}
