package com.retail.management.order.infrastructure.rest.controller;

import com.retail.management.order.application.dto.OrderDetailResponse;
import com.retail.management.order.application.mapper.OrderDetailMapper;
import com.retail.management.order.domain.exception.ExternalApiException;
import com.retail.management.order.domain.exception.OrderNotFoundException;
import com.retail.management.order.domain.model.OrderDetail;
import com.retail.management.order.domain.port.in.OrderDetailServicePort;
import com.retail.management.order.infrastructure.rest.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderDetailControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderDetailServicePort orderDetailService;

    @Mock
    private OrderDetailMapper orderDetailMapper;

    @InjectMocks
    private OrderDetailController orderDetailController;

    private static final String ORDER_REF = "20251216366900020031";

    @BeforeEach
    void setUp() {
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
}
