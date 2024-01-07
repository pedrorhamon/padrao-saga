package br.com.microservices.orchestrated.orchestratorservice.core.saga;

/**
 * @author pedroRhamon
 */
public final class SagaHandler {

	private SagaHandler() {
		
	}
	
	public static final Object[][] SAGA_HANDLER = {
			{1,1,1},
			{1,2,1},
			{2,2,1},
	};
}
