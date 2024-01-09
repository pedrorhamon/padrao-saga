package br.com.microservices.orchestrated.orchestratorservice.core.service;

import org.springframework.stereotype.Service;

import br.com.microservices.orchestrated.orchestratorservice.core.dto.Event;
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
    	
    }
    
	public void continueSaga(Event event) {
		// TODO Auto-generated method stub
		
	}

	public void finishSagaSuccess(Event event) {
		// TODO Auto-generated method stub
		
	}

	public void finishSagaFail(Event event) {
		// TODO Auto-generated method stub
		
	}

}
