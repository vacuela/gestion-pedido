package com.retail.management.order.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response body representing a customer")
public record CustomerResponse(

        @Schema(description = "Unique identifier", example = "665a1b2c3d4e5f6a7b8c9d0e")
        String id,

        @Schema(description = "Customer's first name", example = "Juan")
        String nombre,

        @Schema(description = "Customer's paternal last name", example = "Pérez")
        String apellidoPaterno,

        @Schema(description = "Customer's maternal last name", example = "López")
        String apellidoMaterno,

        @Schema(description = "Customer's email address", example = "juan.perez@example.com")
        String email
) {
}
