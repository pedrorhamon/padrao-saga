package br.com.microservices.orchestrated.productvalidationservice.core.dto;

import java.time.LocalDateTime;

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
public class History {
	
	private String source;
	private ESagaStatus status;
	private String message;
	private LocalDateTime createdAt;

}
