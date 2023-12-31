package br.com.microservices.orchestrated.productvalidationservice.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pedroRhamon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProducts {

	private Product product;
	private int quantity;

}
