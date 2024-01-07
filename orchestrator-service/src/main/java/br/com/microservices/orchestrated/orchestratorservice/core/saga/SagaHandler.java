package br.com.microservices.orchestrated.orchestratorservice.core.saga;

import static br.com.microservices.orchestrated.orchestratorservice.core.enums.EEventSource.*;
import static br.com.microservices.orchestrated.orchestratorservice.core.enums.ESagaStatus.*;
import static br.com.microservices.orchestrated.orchestratorservice.core.enums.ETopics.*;

/**
 * @author pedroRhamon
 */
public final class SagaHandler {

	private SagaHandler() {
		
	}
	
	public static final Object[][] SAGA_HANDLER = {
			{ORCHESTRATOR, SUCCESS, PRODUCT_VALIDATION_SUCCESS},
			{ORCHESTRATOR, FAIL, FINISH_FAIL},
			
			
			{PRODUCT_VALIDATION_SERVICE, ROLLBACK_PENDING, PRODUCT_VALIDATION_FAIL},
			{PRODUCT_VALIDATION_SERVICE, FAIL, FINISH_FAIL},
			{PRODUCT_VALIDATION_SERVICE, SUCCESS, PAYMENT_SUCCESS},
			
			{PRODUCT_VALIDATION_SERVICE, ROLLBACK_PENDING, PRODUCT_VALIDATION_FAIL},
			{PRODUCT_VALIDATION_SERVICE, FAIL, FINISH_FAIL},
			{PRODUCT_VALIDATION_SERVICE, SUCCESS, PAYMENT_SUCCESS},
	};
}
