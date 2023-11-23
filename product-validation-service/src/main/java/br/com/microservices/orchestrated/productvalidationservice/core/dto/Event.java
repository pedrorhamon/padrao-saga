package br.com.microservices.orchestrated.productvalidationservice.core.dto;

import java.time.LocalDateTime;
import java.util.List;

import br.com.microservices.orchestrated.productvalidationservice.core.enums.ESagaStatus;
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
	private ESagaStatus status;
	private List<History> eventHistory;
	private LocalDateTime createdAt;

}
