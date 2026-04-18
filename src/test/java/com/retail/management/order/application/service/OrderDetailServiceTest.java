package com.retail.management.order.application.service;

import com.retail.management.order.domain.exception.OrderNotFoundException;
import com.retail.management.order.domain.model.OrderDetail;
import com.retail.management.order.domain.port.out.ItemApiPort;
import com.retail.management.order.domain.port.out.PedidoApiPort;
import com.retail.management.order.infrastructure.rest.dto.ItemResponse;
import com.retail.management.order.infrastructure.rest.dto.PedidoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderDetailServiceTest {

    @Mock
    private PedidoApiPort pedidoApiPort;

    @Mock
    private ItemApiPort itemApiPort;

    @InjectMocks
    private OrderDetailService orderDetailService;

    private static final String ORDER_REF = "20251216366900020031";

    @Nested
    @DisplayName("getOrderDetailByOrderRef")
    class GetOrderDetailByOrderRef {

        @Test
        @DisplayName("should return order details when order and items exist")
        void shouldReturnOrderDetails() {
            PedidoResponse pedido = new PedidoResponse(
                    ORDER_REF, "user-1", "physical", "2025-12-08",
                    false, false,
                    List.of("20251216366900020031-1171500610", "20251216366900020031-898"),
                    "Liverpool Galerías Toluca", "2"
            );

            ItemResponse item1 = new ItemResponse(
                    "20251216366900020031-1171500610", "1171500610", 3,
                    "Pantalón Levi´s", "Compra en línea", "1"
            );
            ItemResponse item2 = new ItemResponse(
                    "20251216366900020031-898", "898", 1,
                    "Camisa Polo", "Compra en línea", "2"
            );

            when(pedidoApiPort.findPedidoByOrderRef(ORDER_REF)).thenReturn(List.of(pedido));
            when(itemApiPort.findItemByItemId("20251216366900020031-1171500610")).thenReturn(List.of(item1));
            when(itemApiPort.findItemByItemId("20251216366900020031-898")).thenReturn(List.of(item2));

            List<OrderDetail> result = orderDetailService.getOrderDetailByOrderRef(ORDER_REF);

            assertEquals(2, result.size());

            assertEquals("20251216366900020031-1171500610", result.get(0).getItemId());
            assertEquals(3, result.get(0).getQuantity());
            assertEquals("Pantalón Levi´s", result.get(0).getDisplayName());
            assertEquals("physical", result.get(0).getCanal());
            assertEquals("2025-12-08", result.get(0).getOrderStatus());

            assertEquals("20251216366900020031-898", result.get(1).getItemId());
            assertEquals(1, result.get(1).getQuantity());
            assertEquals("Camisa Polo", result.get(1).getDisplayName());
            assertEquals("physical", result.get(1).getCanal());
            assertEquals("2025-12-08", result.get(1).getOrderStatus());

            verify(pedidoApiPort).findPedidoByOrderRef(ORDER_REF);
            verify(itemApiPort).findItemByItemId("20251216366900020031-1171500610");
            verify(itemApiPort).findItemByItemId("20251216366900020031-898");
        }

        @Test
        @DisplayName("should throw OrderNotFoundException when no pedido found")
        void shouldThrowWhenNoPedido() {
            when(pedidoApiPort.findPedidoByOrderRef(ORDER_REF)).thenReturn(Collections.emptyList());

            assertThrows(OrderNotFoundException.class,
                    () -> orderDetailService.getOrderDetailByOrderRef(ORDER_REF));

            verify(pedidoApiPort).findPedidoByOrderRef(ORDER_REF);
            verifyNoInteractions(itemApiPort);
        }

        @Test
        @DisplayName("should throw OrderNotFoundException when pedido list is null")
        void shouldThrowWhenPedidoNull() {
            when(pedidoApiPort.findPedidoByOrderRef(ORDER_REF)).thenReturn(null);

            assertThrows(OrderNotFoundException.class,
                    () -> orderDetailService.getOrderDetailByOrderRef(ORDER_REF));
        }

        @Test
        @DisplayName("should skip items not found and return partial results")
        void shouldSkipItemsNotFound() {
            PedidoResponse pedido = new PedidoResponse(
                    ORDER_REF, "user-1", "physical", "2025-12-08",
                    false, false,
                    List.of("20251216366900020031-1171500610", "20251216366900020031-notfound"),
                    "Liverpool Galerías Toluca", "2"
            );

            ItemResponse item1 = new ItemResponse(
                    "20251216366900020031-1171500610", "1171500610", 3,
                    "Pantalón Levi´s", "Compra en línea", "1"
            );

            when(pedidoApiPort.findPedidoByOrderRef(ORDER_REF)).thenReturn(List.of(pedido));
            when(itemApiPort.findItemByItemId("20251216366900020031-1171500610")).thenReturn(List.of(item1));
            when(itemApiPort.findItemByItemId("20251216366900020031-notfound")).thenReturn(Collections.emptyList());

            List<OrderDetail> result = orderDetailService.getOrderDetailByOrderRef(ORDER_REF);

            assertEquals(1, result.size());
            assertEquals("20251216366900020031-1171500610", result.get(0).getItemId());
        }

        @Test
        @DisplayName("should return empty list when pedido has no items")
        void shouldReturnEmptyWhenNoItems() {
            PedidoResponse pedido = new PedidoResponse(
                    ORDER_REF, "user-1", "physical", "2025-12-08",
                    false, false, null, "Liverpool Galerías Toluca", "2"
            );

            when(pedidoApiPort.findPedidoByOrderRef(ORDER_REF)).thenReturn(List.of(pedido));

            List<OrderDetail> result = orderDetailService.getOrderDetailByOrderRef(ORDER_REF);

            assertTrue(result.isEmpty());
            verifyNoInteractions(itemApiPort);
        }
    }
}
