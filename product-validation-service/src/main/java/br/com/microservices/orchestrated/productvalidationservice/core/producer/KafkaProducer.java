package br.com.microservices.orchestrated.productvalidationservice.core.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pedroRhamon
 */

@Slf4j
@Component
@AllArgsConstructor
public class KafkaProducer {

	private final KafkaTemplate<String, String> kafkaTemplate;
	
	public void sendEvent(String payload, String topic) {
		try {
			log.info("Sending event to topic {} with data {}", topic, payload);
			this.kafkaTemplate.send(topic, payload);
		} catch (Exception e) {
			log.error("Error trying to send data to topic {} with data {}", topic, payload, e);
		}
	}
}
