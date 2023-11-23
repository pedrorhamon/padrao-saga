package br.com.microservices.orchestrated.orderservice.core.document;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pedroRhamon
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {
	
	private String id;
	private String transactionId;
	private String orderId;
	private Order playload;
	private String source;
	private String status;
	private List<History> eventHistory;
	private LocalDateTime createdAt;

}
