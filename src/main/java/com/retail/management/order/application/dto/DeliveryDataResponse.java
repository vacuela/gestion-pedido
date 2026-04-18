package com.retail.management.order.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Cuerpo de respuesta que representa datos de entrega del cliente")
public record DeliveryDataResponse(

        @Schema(description = "Identificador único de los datos de entrega", example = "dd-001")
        String id,

        @Schema(description = "Dirección de envío del cliente", example = "Av. Reforma 123, Col. Centro, CDMX, CP 06000")
        String direccionEnvio
) {
}
