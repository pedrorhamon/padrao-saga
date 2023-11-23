package br.com.microservices.orchestrated.paymentservice.core.enums;

/**
 * @author pedroRhamon
 */
public enum ESagaStatus {

	SUCCESS,
	ROLLBACK_PENDING,
	FAIL
}
