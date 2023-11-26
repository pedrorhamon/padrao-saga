package br.com.microservices.orchestrated.orderservice.core.dto;

import java.util.List;

import br.com.microservices.orchestrated.orderservice.core.document.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pedroRhamon
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
	
	private List<OrderProduct> products;

}
