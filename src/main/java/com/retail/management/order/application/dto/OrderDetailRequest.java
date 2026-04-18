package com.retail.management.order.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(description = "Cuerpo de solicitud para crear o actualizar un detalle de pedido")
public record OrderDetailRequest(

        @NotBlank(message = "orderRef es requerido")
        @Schema(description = "Número de referencia del pedido", example = "3010091676")
        String orderRef,

        @NotBlank(message = "userId es requerido")
        @Schema(description = "Identificador del usuario", example = "75c97531-abf5-4524-8107-90aa48d08efc")
        String userId,

        @NotBlank(message = "canal es requerido")
        @Schema(description = "Canal de venta", example = "online")
        String canal,

        @NotBlank(message = "orderStatus es requerido")
        @Schema(description = "Estado del pedido", example = "2025-12-06")
        String orderStatus,

        @Schema(description = "Indica si es marketplace", example = "false")
        boolean marketPlace,

        @Schema(description = "Indica si es regalo de registro", example = "false")
        boolean giftRegistry,

        @NotEmpty(message = "items es requerido y no puede estar vacío")
        @Valid
        @Schema(description = "Lista de items del pedido")
        List<ItemRequest> items,

        @NotBlank(message = "storeName es requerido")
        @Schema(description = "Nombre de la tienda", example = "L  SANTA FE")
        String storeName
) {
}
