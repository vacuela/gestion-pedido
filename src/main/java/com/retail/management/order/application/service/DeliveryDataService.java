package com.retail.management.order.application.service;

import com.retail.management.order.domain.exception.CustomerNotFoundException;
import com.retail.management.order.domain.exception.DeliveryDataNotFoundException;
import com.retail.management.order.domain.model.Customer;
import com.retail.management.order.domain.model.DeliveryData;
import com.retail.management.order.domain.port.in.DeliveryDataServicePort;
import com.retail.management.order.domain.port.out.CustomerRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DeliveryDataService implements DeliveryDataServicePort {

    private final CustomerRepositoryPort customerRepository;

    public DeliveryDataService(CustomerRepositoryPort customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public DeliveryData addDeliveryData(String customerId, DeliveryData deliveryData) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        deliveryData.setId(UUID.randomUUID().toString());

        List<DeliveryData> datosEntrega = new ArrayList<>(customer.getDatosEntrega());
        datosEntrega.add(deliveryData);
        customer.setDatosEntrega(datosEntrega);

        customerRepository.save(customer);
        return deliveryData;
    }

    @Override
    public List<DeliveryData> findAllByCustomerId(String customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        return customer.getDatosEntrega();
    }

    @Override
    public DeliveryData findByCustomerIdAndDeliveryDataId(String customerId, String deliveryDataId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        return customer.getDatosEntrega().stream()
                .filter(dd -> dd.getId().equals(deliveryDataId))
                .findFirst()
                .orElseThrow(() -> new DeliveryDataNotFoundException(customerId, deliveryDataId));
    }

    @Override
    public DeliveryData updateDeliveryData(String customerId, String deliveryDataId, DeliveryData deliveryData) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        DeliveryData existing = customer.getDatosEntrega().stream()
                .filter(dd -> dd.getId().equals(deliveryDataId))
                .findFirst()
                .orElseThrow(() -> new DeliveryDataNotFoundException(customerId, deliveryDataId));

        existing.setDireccionEnvio(deliveryData.getDireccionEnvio());

        customerRepository.save(customer);
        return existing;
    }

    @Override
    public void deleteDeliveryData(String customerId, String deliveryDataId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        boolean removed = customer.getDatosEntrega().removeIf(dd -> dd.getId().equals(deliveryDataId));

        if (!removed) {
            throw new DeliveryDataNotFoundException(customerId, deliveryDataId);
        }

        customerRepository.save(customer);
    }
}
