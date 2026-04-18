package com.retail.management.order.application.service;

import com.retail.management.order.domain.exception.CustomerNotFoundException;
import com.retail.management.order.domain.exception.DuplicateEmailException;
import com.retail.management.order.domain.model.Customer;
import com.retail.management.order.domain.port.in.CustomerServicePort;
import com.retail.management.order.domain.port.out.CustomerRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements CustomerServicePort {

    private final CustomerRepositoryPort customerRepository;

    public CustomerService(CustomerRepositoryPort customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer create(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new DuplicateEmailException(customer.getEmail());
        }
        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> findById(String id) {
        return customerRepository.findById(id);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer update(String id, Customer customer) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        customerRepository.findByEmail(customer.getEmail())
                .filter(c -> !c.getUserId().equals(id))
                .ifPresent(c -> {
                    throw new DuplicateEmailException(customer.getEmail());
                });

        existing.setNombre(customer.getNombre());
        existing.setApellidoPaterno(customer.getApellidoPaterno());
        existing.setApellidoMaterno(customer.getApellidoMaterno());
        existing.setEmail(customer.getEmail());

        return customerRepository.save(existing);
    }

    @Override
    public void delete(String id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
        customerRepository.deleteById(id);
    }
}
