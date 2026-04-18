package com.retail.management.order.infrastructure.persistence.mongodb.mapper;

import com.retail.management.order.domain.model.Customer;
import com.retail.management.order.infrastructure.persistence.mongodb.document.CustomerDocument;
import org.springframework.stereotype.Component;

@Component
public class CustomerDocumentMapper {

    public CustomerDocument toDocument(Customer customer) {
        return new CustomerDocument(
                customer.getUserId(),
                customer.getNombre(),
                customer.getApellidoPaterno(),
                customer.getApellidoMaterno(),
                customer.getEmail()
        );
    }

    public Customer toDomain(CustomerDocument document) {
        return new Customer(
                document.getUserId(),
                document.getNombre(),
                document.getApellidoPaterno(),
                document.getApellidoMaterno(),
                document.getEmail()
        );
    }
}
