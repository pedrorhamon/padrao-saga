package br.com.microservices.orchestrated.paymentservice.core.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import br.com.microservices.orchestrated.paymentservice.config.exception.ValidationException;
import br.com.microservices.orchestrated.paymentservice.core.dto.Event;
import br.com.microservices.orchestrated.paymentservice.core.dto.History;
import br.com.microservices.orchestrated.paymentservice.core.dto.OrderProduct;
import br.com.microservices.orchestrated.paymentservice.core.enums.EPaymentStatus;
import br.com.microservices.orchestrated.paymentservice.core.enums.ESagaStatus;
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
    private static final Double MIN_VALUE_AMOUNT = 0.1;

	private final PaymentRepository paymentRepository;

	private final JsonUtil jsonUtil;
	private final KafkaProducer producer;

	public void realizePayment(Event event) {
		try {
			this.checkCurrentValidation(event);
			this.createPendingPayment(event);
			var payment = this.findByOrderIdAndTransactionId(event);
			this.validateAmount(payment.getTotalAmount());
			this.changePaymentToSuccess(payment);
			this.handleSuccess(event);
		} catch (Exception e) {
			log.error("Error trying to make payment: ", e);
			this.handleFailCurrentNotExecuted(event, e.getMessage());
		}
		this.producer.sendEvent(this.jsonUtil.toJson(event));
	}
	
	private void validateAmount(double amount) {
        if (amount < MIN_VALUE_AMOUNT) {
            throw new ValidationException("The minimal amount available is ".concat(String.valueOf(MIN_VALUE_AMOUNT)));
        }
    }

	private void createPendingPayment(Event event) {
		 var totalAmount = this.calculateAmount(event);
		 var totalItems = this.calculateTotalItems(event);
		 var payment = Payment.builder()
				 .orderId(event.getPayload().getId())
				 .transactionId(event.getTransactionId())
				 .totalAmount(totalAmount)
				 .totalItems(totalItems)
				 .build();
		 this.save(payment);
		 this.setEventAmountItems(event, payment);
	}
	
	private void setEventAmountItems(Event event, Payment payment) {
		event.getPayload().setTotalAmount(payment.getTotalAmount());
		event.getPayload().setTotalItems(payment.getTotalItems());
	}

	private double calculateAmount(Event event) {
        return event
            .getPayload()
            .getProducts()
            .stream()
            .map(product -> product.getQuantity() * product.getProduct().getUnitValue())
            .reduce(REDUCE_SUM_VALUE, Double::sum);
    }
	
	private int calculateTotalItems(Event event) {
		return event
				.getPayload()
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
	
	private Payment findByOrderIdAndTransactionId(Event event) {
		return this.paymentRepository.findByOrderIdAndTransactionId(event.getPayload().getId(), event.getTransactionId())
				.orElseThrow(()-> new ValidationException("Payment not found by OrdeID and TransactionID"));
	}
	
	private void changePaymentToSuccess(Payment payment) {
		payment.setStatus(EPaymentStatus.SUCCESS);
		this.save(payment);
	}
	
	private void handleSuccess(Event event) {
        event.setStatus(ESagaStatus.SUCCESS);
        event.setSource(CURRENT_SOURCE);
        this.addHistory(event, "Payment realized successfully!");
    }
	
	private void addHistory(Event event, String message) {
        var history = History
            .builder()
            .source(event.getSource())
            .status(event.getStatus())
            .message(message)
            .createdAt(LocalDateTime.now())
            .build();
        event.addToHistory(history);
    }
	
	public void realizeRefund(Event event) {
		event.setStatus(ESagaStatus.FAIL);
		event.setSource(CURRENT_SOURCE);
		try {
			this.changePaymentStatusToRefund(event);
			addHistory(event, "Rollback executed for payment");
		} catch (Exception e) {
			addHistory(event, "Rollback not executed for payment".concat(e.getMessage()));
		}
		
		this.producer.sendEvent(this.jsonUtil.toJson(event));
	}
	
	private void changePaymentStatusToRefund(Event event) {
		var payment = this.findByOrderIdAndTransactionId(event);
		payment.setStatus(EPaymentStatus.REFUND);
		this.setEventAmountItems(event, payment);
		this.save(payment);
	}
	
	 private void handleFailCurrentNotExecuted(Event event, String message) {
	        event.setStatus(ESagaStatus.ROLLBACK_PENDING);
	        event.setSource(CURRENT_SOURCE);
	        addHistory(event, "Fail to realize payment: ".concat(message));
	    }

}
