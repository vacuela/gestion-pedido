package com.retail.management.order.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Cuerpo de solicitud para crear o actualizar un cliente")
public record CustomerRequest(

        @NotBlank(message = "userId es requerido")
        @Size(min = 2, max = 100, message = "userId debe tener entre 2 y 100 caracteres")
        @Schema(description = "Identificador único del Cliente", example = "665a1b2c3d4e5f6a7b8c9d0e")
        String userId,

        @NotBlank(message = "Nombre es requerido")
        @Size(min = 2, max = 100, message = "Nombre debe tener entre 2 y 100 caracteres")
        @Schema(description = "Nombre del Cliente", example = "Juan")
        String nombre,

        @NotBlank(message = "Apellido Paterno es requerido")
        @Size(min = 2, max = 100, message = "Apellido Paterno debe tener entre 2 y 100 caracteres")
        @Schema(description = "Apellido Paterno del Cliente", example = "Pérez")
        String apellidoPaterno,

        @Size(max = 100, message = "Apellido Materno debe tener como máximo 100 caracteres")
        @Schema(description = "Apellido Materno del Cliente", example = "López")
        String apellidoMaterno,

        @NotBlank(message = "Email es requerido")
        @Email(message = "Email debe ser una dirección de correo electrónico válida")
        @Schema(description = "Correo electrónico del Cliente", example = "juan.perez@example.com")
        String email
) {
}
