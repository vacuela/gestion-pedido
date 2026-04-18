package com.retail.management.order.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Detalle de un item dentro del pedido")
public record ItemRequest(

        @NotBlank(message = "itemId es requerido")
        @Schema(description = "Identificador del item", example = "3010091676-1132351437")
        String itemId,

        @NotBlank(message = "skuId es requerido")
        @Schema(description = "SKU del producto", example = "1132351437")
        String skuId,

        @Min(value = 1, message = "quantity debe ser al menos 1")
        @Schema(description = "Cantidad del producto", example = "3")
        int quantity,

        @NotBlank(message = "displayName es requerido")
        @Schema(description = "Nombre del producto", example = "Pantalón Levi´s")
        String displayName,

        @NotBlank(message = "deliveryStatus es requerido")
        @Schema(description = "Estado de entrega", example = "Compra en línea")
        String deliveryStatus
) {
}
