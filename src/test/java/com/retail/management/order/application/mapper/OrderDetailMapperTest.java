package com.retail.management.order.application.mapper;

import com.retail.management.order.application.dto.OrderDetailResponse;
import com.retail.management.order.domain.model.OrderDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderDetailMapperTest {

    private OrderDetailMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new OrderDetailMapper();
    }

    @Test
    @DisplayName("should map OrderDetail domain to OrderDetailResponse")
    void shouldMapToResponse() {
        OrderDetail detail = new OrderDetail(
                "20251216366900020031-1171500610",
                3,
                "Pantalón Levi´s",
                "physical",
                "2025-12-08"
        );

        OrderDetailResponse result = mapper.toResponse(detail);

        assertEquals("20251216366900020031-1171500610", result.itemId());
        assertEquals(3, result.quantity());
        assertEquals("Pantalón Levi´s", result.displayName());
        assertEquals("physical", result.canal());
        assertEquals("2025-12-08", result.orderStatus());
    }
}
