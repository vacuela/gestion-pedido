package com.retail.management.order.infrastructure.rest.dto;

public record PedidoResponse(
        String orderRef,
        String userId
) {
}
