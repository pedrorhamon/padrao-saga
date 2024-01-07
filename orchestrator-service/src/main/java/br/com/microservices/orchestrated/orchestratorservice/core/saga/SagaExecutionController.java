package br.com.microservices.orchestrated.orchestratorservice.core.saga;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import br.com.microservices.orchestrated.orchestratorservice.config.exceptions.ValidationException;
import br.com.microservices.orchestrated.orchestratorservice.core.dto.Event;
import br.com.microservices.orchestrated.orchestratorservice.core.enums.ETopics;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.lang.String.format;

/**
 * @author pedroRhamon
 */
@Slf4j
@Component
@AllArgsConstructor
public class SagaExecutionController {
	
	public ETopics getNextTopic(Event event) {
		if(isEmpty(event.getSource()) || isEmpty(event.getStatus())) {
			throw new ValidationException("Source and status must be informed.");
		}
		
		return this.findTopicBySourceAndStatus(event);
	}
	
	private ETopics findTopicBySourceAndStatus(Event event) {
		return (ETopics) (Arrays.stream(SagaHandler.SAGA_HANDLER)
				.filter(row -> this.isEventSourceAndStatusValid(event, row))
				.map(i -> i[SagaHandler.TOPIC_INDEX])
				.findFirst()
				.orElseThrow(() -> new ValidationException("Topic not found")));
	}
	
	private boolean isEventSourceAndStatusValid(Event event, Object[] row) {
		var source = row[SagaHandler.EVENT_SOURCE_INDEX];
		var status = row[SagaHandler.SAGA_STATUS_INDEX];
		
		return event.getSource().equals(source) && event.getStatus().equals(status);
	}
	
	private void logCurrentSaga(Event event, ETopics topic) {
		var sagaId = this.createSagaId(event);
		var source = event.getSource();
		
		switch (event.getStatus()) {
				case SUCCESS -> log.info("### CURRENT SAGA: {} | SUCCESS | NEXT TOPIC {} | {}",
                source, topic, sagaId);
            case ROLLBACK_PENDING -> log.info("### CURRENT SAGA: {} | SENDING TO ROLLBACK CURRENT SERVICE | NEXT TOPIC {} | {}",
                source, topic, sagaId);
            case FAIL -> log.info("### CURRENT SAGA: {} | SENDING TO ROLLBACK PREVIOUS SERVICE | NEXT TOPIC {} | {}",
                source, topic, sagaId);
	}
	}
	
	private String createSagaId(Event event) {
		return format("ORDER ID: %s | TRANSACTION ID %s | EVENT ID %s", event.getPayload().getId(), event.getTransactionId(), event.getId());
	}
}
