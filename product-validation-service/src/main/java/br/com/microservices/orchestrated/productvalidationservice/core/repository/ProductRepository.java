package br.com.microservices.orchestrated.productvalidationservice.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.microservices.orchestrated.productvalidationservice.core.model.Product;


/**
 * @author pedroRhamon
 */
public interface ProductRepository extends JpaRepository<Product, Integer>{

}
