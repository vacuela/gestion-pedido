package com.retail.management.order.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detalle de un producto dentro de un pedido")
public record OrderDetailResponse(

        @Schema(description = "Código del producto", example = "20251216366900020031-1171500610")
        String itemId,

        @Schema(description = "Cantidad del producto", example = "3")
        int quantity,

        @Schema(description = "Nombre del producto", example = "Pantalón Levi´s")
        String displayName,

        @Schema(description = "Canal de venta", example = "physical")
        String canal,

        @Schema(description = "Estatus del pedido", example = "2025-12-08")
        String orderStatus
) {
}
