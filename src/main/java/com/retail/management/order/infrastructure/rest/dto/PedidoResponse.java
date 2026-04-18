package com.retail.management.order.infrastructure.rest.dto;

import java.util.List;

public record PedidoResponse(
        String orderRef,
        String userId,
        String canal,
        String orderStatus,
        boolean marketPlace,
        boolean giftRegistry,
        List<String> items,
        String storeName,
        String id
) {
}
