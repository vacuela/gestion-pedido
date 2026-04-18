package com.retail.management.order.infrastructure.rest.controller;

import com.retail.management.order.application.dto.OrderDetailResponse;
import com.retail.management.order.application.mapper.OrderDetailMapper;
import com.retail.management.order.domain.model.OrderDetail;
import com.retail.management.order.domain.port.in.OrderDetailServicePort;
import com.retail.management.order.infrastructure.rest.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
}
