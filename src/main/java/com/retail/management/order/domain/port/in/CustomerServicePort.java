package com.retail.management.order.domain.port.in;

import com.retail.management.order.domain.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerServicePort {

    Customer create(Customer customer);

    Optional<Customer> findById(String id);

    List<Customer> findAll();

    Customer update(String id, Customer customer);

    void delete(String id);
}
