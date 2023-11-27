package br.com.microservices.orchestrated.orderservice.core.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.microservices.orchestrated.orderservice.core.document.Event;

/**
 * @author pedroRhamon
 */
public interface EventRepository extends MongoRepository<Event, String>{}
