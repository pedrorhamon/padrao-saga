package br.com.microservices.orchestrated.inventoryservice.core.service;

import org.springframework.stereotype.Service;

import br.com.microservices.orchestrated.inventoryservice.core.dto.Event;
import br.com.microservices.orchestrated.inventoryservice.core.repository.InventoryRepository;
import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */

@Service
@AllArgsConstructor
public class InventoryService {
	
	private final InventoryRepository inventoryRepository;

	public void updateInventory(Event event) {
		// TODO Auto-generated method stub
		
	}

	public void rollbackInventory(Event event) {
		// TODO Auto-generated method stub
		
	}

}
