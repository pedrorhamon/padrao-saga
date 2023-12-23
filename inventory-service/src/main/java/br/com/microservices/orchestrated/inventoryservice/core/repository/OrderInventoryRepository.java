package br.com.microservices.orchestrated.inventoryservice.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.microservices.orchestrated.inventoryservice.core.model.OrderInventory;

/**
 * @author pedroRhamon
 */
public interface OrderInventoryRepository extends JpaRepository<OrderInventory, Integer>{
	
	boolean existsByOrderIdAndTransactionId(String orderId, String transactionID);

	List<OrderInventory> findByOrderIdAndTransactionId(String orderId, String transactionID);

}
