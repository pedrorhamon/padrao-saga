package br.com.microservices.orchestrated.paymentservice.core.service;

import org.springframework.stereotype.Service;

import br.com.microservices.orchestrated.paymentservice.core.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pedroRhamon
 */
@Slf4j
@Service
@AllArgsConstructor
public class PaymentService {
	
    private static final String CURRENT_SOURCE = "PAYMENT_SERVICE";
	
	private final PaymentRepository paymentRepository;

	
}
