package br.com.microservices.orchestrated.productvalidationservice.core.producer;

import org.springframework.beans.factory.annotation.Value;
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

	@Value("${spring.kafka.topic.orchestrator}")
	private String orchestratorTopic;

	public void sendEvent(String payload) {
		try {
			log.info("Sending event to topic {} with data {}", this.orchestratorTopic, payload);
			this.kafkaTemplate.send(this.orchestratorTopic, payload);
		} catch (Exception e) {
			log.error("Error trying to send data to topic {} with data {}", this.orchestratorTopic, payload, e);
		}
	}
}
