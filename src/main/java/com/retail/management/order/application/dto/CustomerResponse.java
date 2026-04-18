package com.retail.management.order.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Cuerpo de respuesta que representa un cliente")
public record CustomerResponse(

        @Schema(description = "Identificador único del Cliente", example = "665a1b2c3d4e5f6a7b8c9d0e")
        String userId,

        @Schema(description = "Nombre del Cliente", example = "Juan")
        String nombre,

        @Schema(description = "Apellido Paterno del Cliente", example = "Pérez")
        String apellidoPaterno,

        @Schema(description = "Apellido Materno del Cliente", example = "López")
        String apellidoMaterno,

        @Schema(description = "Correo electrónico del Cliente", example = "juan.perez@example.com")
        String email
) {
}
