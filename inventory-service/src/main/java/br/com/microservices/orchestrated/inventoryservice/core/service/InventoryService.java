package br.com.microservices.orchestrated.inventoryservice.core.service;

import org.springframework.stereotype.Service;

import br.com.microservices.orchestrated.inventoryservice.core.repository.InventoryRepository;
import lombok.AllArgsConstructor;

/**
 * @author pedroRhamon
 */

@Service
@AllArgsConstructor
public class InventoryService {
	
	private final InventoryRepository inventoryRepository;

}
