package br.com.microservices.orchestrated.paymentservice.core.service;

import org.springframework.stereotype.Service;

import br.com.microservices.orchestrated.paymentservice.config.exception.ValidationException;
import br.com.microservices.orchestrated.paymentservice.core.dto.Event;
import br.com.microservices.orchestrated.paymentservice.core.dto.OrderProduct;
import br.com.microservices.orchestrated.paymentservice.core.model.Payment;
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
	private static final Double REDUCE_SUM_VALUE = 0.0;

	private final PaymentRepository paymentRepository;

	private final JsonUtil jsonUtil;
	private final KafkaProducer producer;

	public void realizePayment(Event event) {
		try {
			this.checkCurrentValidation(event);
			this.createPendingPayment(event);
		} catch (Exception e) {
			log.error("Error trying to make payment: ", e);
		}
		this.producer.sendEvent(this.jsonUtil.toJson(event));
	}

	private void createPendingPayment(Event event) {
		 var totalAmount = this.calculateAmount(event);
		 var totalItems = this.calculateTotalItems(event);
		 var payment = Payment.builder()
				 .orderId(event.getPlayload().getId())
				 .transactionId(event.getTransactionId())
				 .totalAmount(totalAmount)
				 .totalItems(totalItems)
				 .build();
		 this.save(payment);
		 this.setEventAmountItems(event, payment);
	}
	
	private void setEventAmountItems(Event event, Payment payment) {
		event.getPlayload().setTotalAmount(payment.getTotalAmount());
		event.getPlayload().setTotalItems(payment.getTotalItems());
	}

	private double calculateAmount(Event event) {
        return event
            .getPlayload()
            .getProducts()
            .stream()
            .map(product -> product.getQuantity() * product.getProduct().getUnitValue())
            .reduce(REDUCE_SUM_VALUE, Double::sum);
    }
	
	private int calculateTotalItems(Event event) {
		return event
				.getPlayload()
				.getProducts()
				.stream()
				.map(OrderProduct::getQuantity)
				.reduce(REDUCE_SUM_VALUE.intValue(), Integer::sum);
	}

	private void save(Payment payment) {
		this.paymentRepository.save(payment);
	}

	private void checkCurrentValidation(Event event) {
		if (this.paymentRepository.existsByOrderIdAndTransactionId(event.getOrderId(), event.getTransactionId())) {
			throw new ValidationException("There's another transactionId for this validation.");
		}
	}

}
