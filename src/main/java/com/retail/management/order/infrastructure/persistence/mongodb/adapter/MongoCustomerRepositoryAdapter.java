package com.retail.management.order.infrastructure.persistence.mongodb.adapter;

import com.retail.management.order.domain.model.Customer;
import com.retail.management.order.domain.port.out.CustomerRepositoryPort;
import com.retail.management.order.infrastructure.persistence.mongodb.mapper.CustomerDocumentMapper;
import com.retail.management.order.infrastructure.persistence.mongodb.repository.SpringDataMongoCustomerRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MongoCustomerRepositoryAdapter implements CustomerRepositoryPort {

    private final SpringDataMongoCustomerRepository mongoRepository;
    private final CustomerDocumentMapper mapper;

    public MongoCustomerRepositoryAdapter(SpringDataMongoCustomerRepository mongoRepository,
                                          CustomerDocumentMapper mapper) {
        this.mongoRepository = mongoRepository;
        this.mapper = mapper;
    }

    @Override
    public Customer save(Customer customer) {
        var document = mapper.toDocument(customer);
        var saved = mongoRepository.save(document);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Customer> findById(String id) {
        return mongoRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Customer> findAll() {
        return mongoRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return mongoRepository.existsById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return mongoRepository.existsByEmail(email);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return mongoRepository.findByEmail(email).map(mapper::toDomain);
    }
}
