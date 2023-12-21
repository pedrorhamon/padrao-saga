package br.com.microservices.orchestrated.inventoryservice.core.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pedroRhamon
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_inventory")
public class OrderInventory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "inventory_id", nullable = false)
	private Inventory inventory;

	@Column(nullable = false)
	private String orderId;

	@Column(nullable = false)
	private String transactionId;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updateAt;
	
	@PrePersist
	public void prePersist() {
		var now = LocalDateTime.now();
		createdAt = now;
		updateAt = now;
	}

	@PreUpdate
	public void preUpdate() {
		updateAt = LocalDateTime.now();
	}

}
