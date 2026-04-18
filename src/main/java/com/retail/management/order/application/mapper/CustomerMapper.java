package com.retail.management.order.application.mapper;

import com.retail.management.order.application.dto.CustomerRequest;
import com.retail.management.order.application.dto.CustomerResponse;
import com.retail.management.order.domain.model.Customer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CustomerMapper {

    public Customer toDomain(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setUserId(request.userId());
        customer.setNombre(request.nombre());
        customer.setApellidoPaterno(request.apellidoPaterno());
        customer.setApellidoMaterno(request.apellidoMaterno());
        customer.setEmail(request.email());
        return customer;
    }

    public CustomerResponse toResponse(Customer customer) {
        return toResponse(customer, Collections.emptyList());
    }

    public CustomerResponse toResponse(Customer customer, List<String> orders) {
        return new CustomerResponse(
                customer.getUserId(),
                customer.getNombre(),
                customer.getApellidoPaterno(),
                customer.getApellidoMaterno(),
                customer.getEmail(),
                orders
        );
    }
}
