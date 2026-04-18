package com.retail.management.order.infrastructure.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.management.order.application.dto.ItemRequest;
import com.retail.management.order.application.dto.OrderDetailRequest;
import com.retail.management.order.application.dto.OrderDetailResponse;
import com.retail.management.order.application.mapper.OrderDetailMapper;
import com.retail.management.order.domain.exception.ExternalApiException;
import com.retail.management.order.domain.exception.OrderNotFoundException;
import com.retail.management.order.domain.model.OrderDetail;
import com.retail.management.order.domain.port.in.OrderDetailServicePort;
import com.retail.management.order.infrastructure.rest.dto.PedidoResponse;
import com.retail.management.order.infrastructure.rest.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderDetailControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private OrderDetailServicePort orderDetailService;

    @Mock
    private OrderDetailMapper orderDetailMapper;

    @InjectMocks
    private OrderDetailController orderDetailController;

    private static final String ORDER_REF = "20251216366900020031";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(orderDetailController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("GET /api/v1/order-details?orderRef={orderRef}")
    class GetOrderDetail {

        @Test
        @DisplayName("should return order details when order exists")
        void shouldReturnOrderDetails() throws Exception {
            OrderDetail detail1 = new OrderDetail(
                    "20251216366900020031-1171500610", 3, "Pantalón Levi´s", "physical", "2025-12-08"
            );
            OrderDetail detail2 = new OrderDetail(
                    "20251216366900020031-898", 1, "Camisa Polo", "physical", "2025-12-08"
            );

            OrderDetailResponse response1 = new OrderDetailResponse(
                    "20251216366900020031-1171500610", 3, "Pantalón Levi´s", "physical", "2025-12-08"
            );
            OrderDetailResponse response2 = new OrderDetailResponse(
                    "20251216366900020031-898", 1, "Camisa Polo", "physical", "2025-12-08"
            );

            when(orderDetailService.getOrderDetailByOrderRef(ORDER_REF))
                    .thenReturn(List.of(detail1, detail2));
            when(orderDetailMapper.toResponse(detail1)).thenReturn(response1);
            when(orderDetailMapper.toResponse(detail2)).thenReturn(response2);

            mockMvc.perform(get("/api/v1/order-details")
                            .param("orderRef", ORDER_REF))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].itemId").value("20251216366900020031-1171500610"))
                    .andExpect(jsonPath("$[0].quantity").value(3))
                    .andExpect(jsonPath("$[0].displayName").value("Pantalón Levi´s"))
                    .andExpect(jsonPath("$[0].canal").value("physical"))
                    .andExpect(jsonPath("$[0].orderStatus").value("2025-12-08"))
                    .andExpect(jsonPath("$[1].itemId").value("20251216366900020031-898"))
                    .andExpect(jsonPath("$[1].quantity").value(1))
                    .andExpect(jsonPath("$[1].displayName").value("Camisa Polo"));
        }

        @Test
        @DisplayName("should return 404 when order not found")
        void shouldReturn404WhenOrderNotFound() throws Exception {
            when(orderDetailService.getOrderDetailByOrderRef("INVALID"))
                    .thenThrow(new OrderNotFoundException("INVALID"));

            mockMvc.perform(get("/api/v1/order-details")
                            .param("orderRef", "INVALID"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404));
        }

        @Test
        @DisplayName("should return 502 when external API fails")
        void shouldReturn502WhenExternalApiFails() throws Exception {
            when(orderDetailService.getOrderDetailByOrderRef(ORDER_REF))
                    .thenThrow(new ExternalApiException("Error fetching order data"));

            mockMvc.perform(get("/api/v1/order-details")
                            .param("orderRef", ORDER_REF))
                    .andExpect(status().isBadGateway())
                    .andExpect(jsonPath("$.status").value(502));
        }

        @Test
        @DisplayName("should return 400 when orderRef param is missing")
        void shouldReturn400WhenMissingParam() throws Exception {
            mockMvc.perform(get("/api/v1/order-details"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/order-details")
    class CreateOrderDetail {

        @Test
        @DisplayName("should create order detail and return 201")
        void shouldCreateOrderDetail() throws Exception {
            OrderDetailRequest request = new OrderDetailRequest(
                    "3010091676", "75c97531-abf5-4524-8107-90aa48d08efc",
                    "online", "2025-12-06", false, false,
                    List.of(
                            new ItemRequest("3010091676-1132351437", "1132351437", 3, "Pantalón Levi´s", "Compra en línea"),
                            new ItemRequest("3010091676-1179743767", "1179743767", 1, "Camisa Polo", "Compra en línea")
                    ),
                    "L  SANTA FE"
            );

            PedidoResponse pedidoResponse = new PedidoResponse(
                    "3010091676", "75c97531-abf5-4524-8107-90aa48d08efc",
                    "online", "2025-12-06", false, false,
                    List.of("3010091676-1132351437", "3010091676-1179743767"),
                    "L  SANTA FE", "10"
            );

            when(orderDetailService.createOrderDetail(any(OrderDetailRequest.class)))
                    .thenReturn(pedidoResponse);

            mockMvc.perform(post("/api/v1/order-details")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.orderRef").value("3010091676"))
                    .andExpect(jsonPath("$.userId").value("75c97531-abf5-4524-8107-90aa48d08efc"))
                    .andExpect(jsonPath("$.canal").value("online"))
                    .andExpect(jsonPath("$.storeName").value("L  SANTA FE"))
                    .andExpect(jsonPath("$.items.length()").value(2));

            verify(orderDetailService).createOrderDetail(any(OrderDetailRequest.class));
        }

        @Test
        @DisplayName("should return 400 when request body is invalid")
        void shouldReturn400WhenInvalidRequest() throws Exception {
            OrderDetailRequest request = new OrderDetailRequest(
                    "", "", "", "", false, false, List.of(), ""
            );

            mockMvc.perform(post("/api/v1/order-details")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 502 when external API fails during creation")
        void shouldReturn502WhenExternalApiFailsDuringCreation() throws Exception {
            OrderDetailRequest request = new OrderDetailRequest(
                    "3010091676", "75c97531-abf5-4524-8107-90aa48d08efc",
                    "online", "2025-12-06", false, false,
                    List.of(new ItemRequest("3010091676-1132351437", "1132351437", 3, "Pantalón Levi´s", "Compra en línea")),
                    "L  SANTA FE"
            );

            when(orderDetailService.createOrderDetail(any(OrderDetailRequest.class)))
                    .thenThrow(new ExternalApiException("Error creating order"));

            mockMvc.perform(post("/api/v1/order-details")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadGateway())
                    .andExpect(jsonPath("$.status").value(502));
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/order-details?orderRef={orderRef}")
    class UpdateOrderDetail {

        @Test
        @DisplayName("should update order detail and return 200")
        void shouldUpdateOrderDetail() throws Exception {
            OrderDetailRequest request = new OrderDetailRequest(
                    "3010091676", "75c97531-abf5-4524-8107-90aa48d08efc",
                    "online", "2025-12-06", false, false,
                    List.of(new ItemRequest("3010091676-1132351437", "1132351437", 5, "Pantalón Levi´s", "Compra en línea")),
                    "L  SANTA FE"
            );

            PedidoResponse pedidoResponse = new PedidoResponse(
                    "3010091676", "75c97531-abf5-4524-8107-90aa48d08efc",
                    "online", "2025-12-06", false, false,
                    List.of("3010091676-1132351437"),
                    "L  SANTA FE", "10"
            );

            when(orderDetailService.updateOrderDetail(eq("3010091676"), any(OrderDetailRequest.class)))
                    .thenReturn(pedidoResponse);

            mockMvc.perform(put("/api/v1/order-details")
                            .param("orderRef", "3010091676")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.orderRef").value("3010091676"))
                    .andExpect(jsonPath("$.canal").value("online"))
                    .andExpect(jsonPath("$.items.length()").value(1));

            verify(orderDetailService).updateOrderDetail(eq("3010091676"), any(OrderDetailRequest.class));
        }

        @Test
        @DisplayName("should return 404 when order not found during update")
        void shouldReturn404WhenOrderNotFoundDuringUpdate() throws Exception {
            OrderDetailRequest request = new OrderDetailRequest(
                    "INVALID", "75c97531-abf5-4524-8107-90aa48d08efc",
                    "online", "2025-12-06", false, false,
                    List.of(new ItemRequest("INVALID-1132351437", "1132351437", 3, "Pantalón Levi´s", "Compra en línea")),
                    "L  SANTA FE"
            );

            when(orderDetailService.updateOrderDetail(eq("INVALID"), any(OrderDetailRequest.class)))
                    .thenThrow(new OrderNotFoundException("INVALID"));

            mockMvc.perform(put("/api/v1/order-details")
                            .param("orderRef", "INVALID")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404));
        }

        @Test
        @DisplayName("should return 400 when request body is invalid for update")
        void shouldReturn400WhenInvalidUpdateRequest() throws Exception {
            OrderDetailRequest request = new OrderDetailRequest(
                    "", "", "", "", false, false, List.of(), ""
            );

            mockMvc.perform(put("/api/v1/order-details")
                            .param("orderRef", "3010091676")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }
}
