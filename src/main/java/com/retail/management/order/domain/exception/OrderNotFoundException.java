package com.retail.management.order.domain.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String orderRef) {
        super("Order not found with orderRef: " + orderRef);
    }
}
