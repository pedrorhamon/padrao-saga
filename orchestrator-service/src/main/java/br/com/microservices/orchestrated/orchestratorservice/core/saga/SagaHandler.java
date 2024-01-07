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
			
			{PAYMENT_SERVICE, ROLLBACK_PENDING, PAYMENT_FAIL},
			{PAYMENT_SERVICE, FAIL, PRODUCT_VALIDATION_FAIL},
			{PAYMENT_SERVICE, SUCCESS, INVENTORY_SUCCESS},
			
			{INVENTORY_SERVICE, ROLLBACK_PENDING, INVENTORY_FAIL},
			{INVENTORY_SERVICE, FAIL, PAYMENT_FAIL},
			{INVENTORY_SERVICE, SUCCESS, FINISH_SUCCESS},
	};
}
