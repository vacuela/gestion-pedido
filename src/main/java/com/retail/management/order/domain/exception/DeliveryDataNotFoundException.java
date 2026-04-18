package com.retail.management.order.domain.exception;

public class DeliveryDataNotFoundException extends RuntimeException {

    public DeliveryDataNotFoundException(String customerId, String deliveryDataId) {
        super("Delivery data not found with id: " + deliveryDataId + " for customer: " + customerId);
    }
}
