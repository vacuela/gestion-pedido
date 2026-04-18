package com.retail.management.order.infrastructure.rest.controller;

import com.retail.management.order.application.dto.OrderDetailRequest;
import com.retail.management.order.application.dto.OrderDetailResponse;
import com.retail.management.order.application.mapper.OrderDetailMapper;
import com.retail.management.order.domain.model.OrderDetail;
import com.retail.management.order.domain.port.in.OrderDetailServicePort;
import com.retail.management.order.infrastructure.rest.dto.ErrorResponse;
import com.retail.management.order.infrastructure.rest.dto.PedidoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/v1/order-details")
@Tag(name = "Order Details", description = "API para consulta de detalle de pedido")
public class OrderDetailController {

    private final OrderDetailServicePort orderDetailService;
    private final OrderDetailMapper orderDetailMapper;

    public OrderDetailController(OrderDetailServicePort orderDetailService, OrderDetailMapper orderDetailMapper) {
        this.orderDetailService = orderDetailService;
        this.orderDetailMapper = orderDetailMapper;
    }

    @GetMapping
    @Operation(summary = "Get order detail by order reference",
            description = "Retrieves the product details of an order by its order reference number")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Error communicating with external API",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<List<OrderDetailResponse>> getOrderDetail(
            @Parameter(description = "Order reference number", example = "20251216366900020031", required = true)
            @RequestParam String orderRef) {

        List<OrderDetail> details = orderDetailService.getOrderDetailByOrderRef(orderRef);

        List<OrderDetailResponse> response = details.stream()
                .map(orderDetailMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create order detail",
            description = "Creates a new order by posting to /pedidos and each item to /items")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order detail created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Error communicating with external API",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PedidoResponse> createOrderDetail(
            @Valid @RequestBody OrderDetailRequest request) {

        PedidoResponse created = orderDetailService.createOrderDetail(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping
    @Operation(summary = "Update order detail",
            description = "Updates an existing order by orderRef, updating /pedidos and re-creating /items")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order detail updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "Error communicating with external API",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<PedidoResponse> updateOrderDetail(
            @Parameter(description = "Order reference number", example = "3010091676", required = true)
            @RequestParam String orderRef,
            @Valid @RequestBody OrderDetailRequest request) {

        PedidoResponse updated = orderDetailService.updateOrderDetail(orderRef, request);
        return ResponseEntity.ok(updated);
    }
}
