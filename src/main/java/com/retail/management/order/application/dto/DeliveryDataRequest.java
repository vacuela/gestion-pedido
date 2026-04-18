package com.retail.management.order.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Cuerpo de solicitud para crear o actualizar datos de entrega")
public record DeliveryDataRequest(

        @NotBlank(message = "Dirección de envío es requerida")
        @Size(min = 5, max = 500, message = "Dirección de envío debe tener entre 5 y 500 caracteres")
        @Schema(description = "Dirección de envío del cliente", example = "Av. Reforma 123, Col. Centro, CDMX, CP 06000")
        String direccionEnvio
) {
}
