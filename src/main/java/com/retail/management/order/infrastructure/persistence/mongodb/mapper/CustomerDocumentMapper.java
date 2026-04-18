package com.retail.management.order.infrastructure.persistence.mongodb.mapper;

import com.retail.management.order.domain.model.Customer;
import com.retail.management.order.domain.model.DeliveryData;
import com.retail.management.order.infrastructure.persistence.mongodb.document.CustomerDocument;
import com.retail.management.order.infrastructure.persistence.mongodb.document.DeliveryDataDocument;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerDocumentMapper {

    public CustomerDocument toDocument(Customer customer) {
        CustomerDocument document = new CustomerDocument(
                customer.getUserId(),
                customer.getNombre(),
                customer.getApellidoPaterno(),
                customer.getApellidoMaterno(),
                customer.getEmail()
        );
        if (customer.getDatosEntrega() != null) {
            document.setDatosEntrega(customer.getDatosEntrega().stream()
                    .map(dd -> new DeliveryDataDocument(dd.getId(), dd.getDireccionEnvio()))
                    .toList());
        }
        return document;
    }

    public Customer toDomain(CustomerDocument document) {
        Customer customer = new Customer(
                document.getUserId(),
                document.getNombre(),
                document.getApellidoPaterno(),
                document.getApellidoMaterno(),
                document.getEmail()
        );
        if (document.getDatosEntrega() != null) {
            List<DeliveryData> datosEntrega = new ArrayList<>(document.getDatosEntrega().stream()
                    .map(dd -> new DeliveryData(dd.getId(), dd.getDireccionEnvio()))
                    .toList());
            customer.setDatosEntrega(datosEntrega);
        }
        return customer;
    }
}
