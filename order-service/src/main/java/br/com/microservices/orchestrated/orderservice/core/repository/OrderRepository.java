package br.com.microservices.orchestrated.orderservice.core.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.microservices.orchestrated.orderservice.core.document.Order;

/**
 * @author pedroRhamon
 */
public interface OrderRepository extends MongoRepository<Order, String>{}
