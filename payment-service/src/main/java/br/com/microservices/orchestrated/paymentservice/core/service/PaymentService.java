package br.com.microservices.orchestrated.paymentservice.core.service;

import org.springframework.stereotype.Service;

import br.com.microservices.orchestrated.paymentservice.core.dto.Event;
import br.com.microservices.orchestrated.paymentservice.core.producer.KafkaProducer;
import br.com.microservices.orchestrated.paymentservice.core.repository.PaymentRepository;
import br.com.microservices.orchestrated.paymentservice.core.utils.JsonUtil;
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

	private final JsonUtil jsonUtil;
	private final KafkaProducer producer;
	
	public void realizePayment(Event event) {
		try {
			
		} catch (Exception e) {
            log.error("Error trying to make payment: ", e);
		}
		this.producer.sendEvent(this.jsonUtil.toJson(event));
	}

}
