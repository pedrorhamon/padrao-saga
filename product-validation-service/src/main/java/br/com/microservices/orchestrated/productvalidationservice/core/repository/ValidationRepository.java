package br.com.microservices.orchestrated.productvalidationservice.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.microservices.orchestrated.productvalidationservice.core.model.Validation;

/**
 * @author pedroRhamon
 */
public interface ValidationRepository extends JpaRepository<Validation, Integer>{

	boolean existsByOrderIdAndTransactionId(String orderId, String transactionID);
}
