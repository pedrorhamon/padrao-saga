package br.com.microservices.orchestrated.orchestratorservice.core.dto;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.microservices.orchestrated.orchestratorservice.core.enums.EEventSource;
import br.com.microservices.orchestrated.orchestratorservice.core.enums.ESagaStatus;
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
	private Order payload;
	private EEventSource source;
	private ESagaStatus status;
	private List<History> eventHistory;
	private LocalDateTime createdAt;
	
	public void addToHistory(History history) {
		if (isEmpty(eventHistory)) {
			eventHistory = new ArrayList<>();
		}
	}
}
