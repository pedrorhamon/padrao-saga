package br.com.microservices.orchestrated.orchestratorservice.core.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.com.microservices.orchestrated.orchestratorservice.core.dto.Event;
import br.com.microservices.orchestrated.orchestratorservice.core.dto.History;
import br.com.microservices.orchestrated.orchestratorservice.core.enums.EEventSource;
import br.com.microservices.orchestrated.orchestratorservice.core.enums.ESagaStatus;
import br.com.microservices.orchestrated.orchestratorservice.core.enums.ETopics;
import br.com.microservices.orchestrated.orchestratorservice.core.producer.SagaOrchestratorProducer;
import br.com.microservices.orchestrated.orchestratorservice.core.saga.SagaExecutionController;
import br.com.microservices.orchestrated.orchestratorservice.core.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pedroRhamon
 */

@Slf4j
@Service
@AllArgsConstructor
public class OrchestrationService {
	
	private final SagaOrchestratorProducer producer;
    private final JsonUtil jsonUtil;
    private final SagaExecutionController sagaExecutionController;
    
    public void startSaga(Event event) {
    	event.setSource(EEventSource.ORCHESTRATOR);
    	event.setStatus(ESagaStatus.SUCCESS);
    	var topic = this.getTopics(event);
    	log.info("SAGA STARTED!");
    	this.addHistory(event, "Saga started");
    	this.sendToProducerWithTopic(event, topic);
    	
    }
    
	public void continueSaga(Event event) {
		var topic = this.getTopics(event);
		log.info("SAGA CONTINUANG FOR EVENT {} ", event.getId());
		this.sendToProducerWithTopic(event, topic);
	}

	public void finishSagaSuccess(Event event) {
		event.setSource(EEventSource.ORCHESTRATOR);
    	event.setStatus(ESagaStatus.SUCCESS);
    	log.info("SAGA FINISHED SUCCESSFULLY FOR EVENT {}", event.getId());
    	this.addHistory(event, "Saga finished successfully!");
    	this.notifyFinishedSaga(event);
	}

	public void finishSagaFail(Event event) {
		event.setSource(EEventSource.ORCHESTRATOR);
    	event.setStatus(ESagaStatus.FAIL);
    	log.info("SAGA FINISHED WITH ERRORS FOR EVENT {}", event.getId());
    	this.addHistory(event, "Saga finished with errors!");
    	this.notifyFinishedSaga(event);
	}
	
	private ETopics getTopics(Event event) {
		return this.sagaExecutionController.getNextTopic(event);
	}
	
	private void addHistory(Event event, String message) {
        var history = History
            .builder()
            .source(event.getSource())
            .status(event.getStatus())
            .message(message)
            .createdAt(LocalDateTime.now())
            .build();
        event.addToHistory(history);
    }
	
	private void notifyFinishedSaga(Event event) {
		this.producer.sendEvent(this.jsonUtil.toJson(event), ETopics.NOTIFY_ENDING.getTopic());
	}
	
	private void sendToProducerWithTopic(Event event, ETopics topic) {
		this.producer.sendEvent(this.jsonUtil.toJson(event), topic.getTopic());
	}
}
