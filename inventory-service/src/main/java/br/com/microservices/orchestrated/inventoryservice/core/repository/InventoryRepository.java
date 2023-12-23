package br.com.microservices.orchestrated.inventoryservice.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.microservices.orchestrated.inventoryservice.core.model.Inventory;

/**
 * @author pedroRhamon
 */
public interface InventoryRepository extends JpaRepository<Inventory, Integer>{
	
	Optional<Inventory> findByProductCode(String productCode);

}
