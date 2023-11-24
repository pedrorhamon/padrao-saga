package br.com.microservices.orchestrated.orderservice.core.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pedroRhamon
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class SagaProducer {

	private final KafkaTemplate<String, String> kafkaTemplate;
	
	@Value("${spring.kafka.topic.start-saga}")
	private String startSagaTopic;
	
	public void sendEvent(String payload) {
		try {
			log.info("Sending event to topic {} with data {}", startSagaTopic, payload);
			this.kafkaTemplate.send(this.startSagaTopic, payload);
		} catch (Exception e) {
			log.error("Error trying to send data to topic {} with data {}", this.startSagaTopic, payload, e);
		}
	}
}
