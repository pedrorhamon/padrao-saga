package br.com.microservices.orchestrated.orderservice.core.document;

import java.time.LocalDateTime;

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
	private String status;
	private String message;
	private LocalDateTime createdAt;

}
