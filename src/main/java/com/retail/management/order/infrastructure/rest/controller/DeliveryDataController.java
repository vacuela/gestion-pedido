package com.retail.management.order.infrastructure.rest.controller;

import com.retail.management.order.application.dto.DeliveryDataRequest;
import com.retail.management.order.application.dto.DeliveryDataResponse;
import com.retail.management.order.application.mapper.DeliveryDataMapper;
import com.retail.management.order.domain.model.DeliveryData;
import com.retail.management.order.domain.port.in.DeliveryDataServicePort;
import com.retail.management.order.infrastructure.rest.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/delivery-data")
@Tag(name = "Delivery Data", description = "Customer delivery data management API")
public class DeliveryDataController {

    private final DeliveryDataServicePort deliveryDataService;
    private final DeliveryDataMapper deliveryDataMapper;

    public DeliveryDataController(DeliveryDataServicePort deliveryDataService, DeliveryDataMapper deliveryDataMapper) {
        this.deliveryDataService = deliveryDataService;
        this.deliveryDataMapper = deliveryDataMapper;
    }

    @PostMapping
    @Operation(summary = "Add delivery data", description = "Adds delivery data to an existing customer")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Delivery data created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<DeliveryDataResponse> addDeliveryData(@PathVariable String customerId,
                                                                 @Valid @RequestBody DeliveryDataRequest request) {
        DeliveryData deliveryData = deliveryDataMapper.toDomain(request);
        DeliveryData created = deliveryDataService.addDeliveryData(customerId, deliveryData);
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryDataMapper.toResponse(created));
    }

    @GetMapping
    @Operation(summary = "Get all delivery data", description = "Retrieves all delivery data for a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of delivery data"),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<DeliveryDataResponse>> findAll(@PathVariable String customerId) {
        List<DeliveryDataResponse> responses = deliveryDataService.findAllByCustomerId(customerId).stream()
                .map(deliveryDataMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{deliveryDataId}")
    @Operation(summary = "Get delivery data by ID", description = "Retrieves specific delivery data for a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Delivery data found"),
            @ApiResponse(responseCode = "404", description = "Customer or delivery data not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<DeliveryDataResponse> findById(@PathVariable String customerId,
                                                          @PathVariable String deliveryDataId) {
        DeliveryData deliveryData = deliveryDataService.findByCustomerIdAndDeliveryDataId(customerId, deliveryDataId);
        return ResponseEntity.ok(deliveryDataMapper.toResponse(deliveryData));
    }

    @PutMapping("/{deliveryDataId}")
    @Operation(summary = "Update delivery data", description = "Updates existing delivery data for a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Delivery data updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Customer or delivery data not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<DeliveryDataResponse> update(@PathVariable String customerId,
                                                        @PathVariable String deliveryDataId,
                                                        @Valid @RequestBody DeliveryDataRequest request) {
        DeliveryData deliveryData = deliveryDataMapper.toDomain(request);
        DeliveryData updated = deliveryDataService.updateDeliveryData(customerId, deliveryDataId, deliveryData);
        return ResponseEntity.ok(deliveryDataMapper.toResponse(updated));
    }

    @DeleteMapping("/{deliveryDataId}")
    @Operation(summary = "Delete delivery data", description = "Deletes delivery data from a customer")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Delivery data deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer or delivery data not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> delete(@PathVariable String customerId,
                                        @PathVariable String deliveryDataId) {
        deliveryDataService.deleteDeliveryData(customerId, deliveryDataId);
        return ResponseEntity.noContent().build();
    }
}
