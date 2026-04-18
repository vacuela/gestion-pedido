package com.retail.management.order.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for creating or updating a customer")
public record CustomerRequest(

        @NotBlank(message = "Nombre is required")
        @Size(min = 2, max = 100, message = "Nombre must be between 2 and 100 characters")
        @Schema(description = "Customer's first name", example = "Juan")
        String nombre,

        @NotBlank(message = "Apellido Paterno is required")
        @Size(min = 2, max = 100, message = "Apellido Paterno must be between 2 and 100 characters")
        @Schema(description = "Customer's paternal last name", example = "Pérez")
        String apellidoPaterno,

        @Size(max = 100, message = "Apellido Materno must be at most 100 characters")
        @Schema(description = "Customer's maternal last name", example = "López")
        String apellidoMaterno,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid email address")
        @Schema(description = "Customer's email address", example = "juan.perez@example.com")
        String email
) {
}
