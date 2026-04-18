package com.retail.management.order.infrastructure.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard error response")
public record ErrorResponse(

        @Schema(description = "HTTP status code", example = "400")
        int status,

        @Schema(description = "Error message", example = "Validation failed")
        String message,

        @Schema(description = "Timestamp of the error")
        LocalDateTime timestamp,

        @Schema(description = "List of field validation errors")
        List<FieldError> errors
) {

    public ErrorResponse(int status, String message) {
        this(status, message, LocalDateTime.now(), null);
    }

    public ErrorResponse(int status, String message, List<FieldError> errors) {
        this(status, message, LocalDateTime.now(), errors);
    }

    @Schema(description = "Field-level validation error")
    public record FieldError(

            @Schema(description = "Field name", example = "email")
            String field,

            @Schema(description = "Validation error message", example = "Email is required")
            String message
    ) {
    }
}
