package br.com.microservices.orchestrated.paymentservice.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.microservices.orchestrated.paymentservice.core.model.Payment;

/**
 * @author pedroRhamon
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	boolean existsByOrderIdAndTransactionId(String orderId, String transactionID);

	Optional<Payment> findByOrderIdAndTransactionId(String orderId, String transactionID);

}
