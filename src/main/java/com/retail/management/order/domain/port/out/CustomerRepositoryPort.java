package com.retail.management.order.domain.port.out;

import com.retail.management.order.domain.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepositoryPort {

    Customer save(Customer customer);

    Optional<Customer> findById(String id);

    List<Customer> findAll();

    void deleteById(String id);

    boolean existsById(String id);

    boolean existsByEmail(String email);

    Optional<Customer> findByEmail(String email);
}
