package com.retail.management.order.infrastructure.persistence.mongodb.repository;

import com.retail.management.order.infrastructure.persistence.mongodb.document.CustomerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SpringDataMongoCustomerRepository extends MongoRepository<CustomerDocument, String> {

    Optional<CustomerDocument> findByEmail(String email);

    boolean existsByEmail(String email);
}
