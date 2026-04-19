package com.retail.management.order.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Resultado de búsqueda de pedido con información de productos")
public record OrderSearchResponse(

        @Schema(description = "Número de referencia del pedido", example = "3010091676")
        String orderRef,

        @Schema(description = "Identificador del usuario", example = "75c97531-abf5-4524-8107-90aa48d08efc")
        String userId,

        @Schema(description = "Canal de venta", example = "online")
        String canal,

        @Schema(description = "Estado del pedido", example = "2025-12-06")
        String orderStatus,

        @Schema(description = "Indica si es marketplace", example = "false")
        boolean marketPlace,

        @Schema(description = "Indica si es regalo de registro", example = "false")
        boolean giftRegistry,

        @Schema(description = "Nombre de la tienda", example = "L  SANTA FE")
        String storeName,

        @Schema(description = "Lista de productos con nombre y detalle")
        List<ItemDetail> items
) {

    @Schema(description = "Detalle de un producto dentro del pedido")
    public record ItemDetail(

            @Schema(description = "Identificador del item", example = "3010091676-1132351437")
            String itemId,

            @Schema(description = "SKU del producto", example = "1132351437")
            String skuId,

            @Schema(description = "Cantidad del producto", example = "3")
            int quantity,

            @Schema(description = "Nombre del producto", example = "Laptop Lenovo thinkpad")
            String displayName,

            @Schema(description = "Estado de entrega", example = "Pedido entregado")
            String deliveryStatus
    ) {
    }
}
