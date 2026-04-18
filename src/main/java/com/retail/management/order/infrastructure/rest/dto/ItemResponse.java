package com.retail.management.order.infrastructure.rest.dto;

public record ItemResponse(
        String itemId,
        String skuId,
        int quantity,
        String displayName,
        String deliveryStatus,
        String id
) {
}
