package br.com.microservices.orchestrated.orchestratorservice.core.saga;

import org.springframework.stereotype.Component;
import static org.springframework.util.ObjectUtils.*;

import br.com.microservices.orchestrated.orchestratorservice.config.exceptions.ValidationException;
import br.com.microservices.orchestrated.orchestratorservice.core.dto.Event;
import br.com.microservices.orchestrated.orchestratorservice.core.enums.ETopics;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
		
		return ETopics.BASE_ORCHESTRATOR;
	}
	
	private ETopics findTopicBySourceAndStatus(Event event) {
		
	}
}
