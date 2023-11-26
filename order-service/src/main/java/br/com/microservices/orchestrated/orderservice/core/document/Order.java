package br.com.microservices.orchestrated.orderservice.core.document;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(collection = "order")
public class Order {
	
	@Id
	private String id;
	private List<OrderProduct> products;
	private LocalDateTime createdAt;
	private String transactionId;
	private double totalAmount;
	private int totalItems;

}
