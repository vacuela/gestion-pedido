package com.retail.management.order.application.service;

import com.retail.management.order.application.dto.ItemRequest;
import com.retail.management.order.application.dto.OrderDetailRequest;
import com.retail.management.order.application.dto.OrderSearchResponse;
import com.retail.management.order.domain.exception.OrderNotFoundException;
import com.retail.management.order.domain.model.OrderDetail;
import com.retail.management.order.domain.port.out.ItemApiPort;
import com.retail.management.order.domain.port.out.PedidoApiPort;
import com.retail.management.order.infrastructure.rest.dto.ItemResponse;
import com.retail.management.order.infrastructure.rest.dto.PedidoResponse;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.any;
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

    @Nested
    @DisplayName("createOrderDetail")
    class CreateOrderDetail {

        @Test
        @DisplayName("should create pedido and items successfully")
        void shouldCreatePedidoAndItems() {
            OrderDetailRequest request = new OrderDetailRequest(
                    "3010091676", "75c97531-abf5-4524-8107-90aa48d08efc",
                    "online", "2025-12-06", false, false,
                    List.of(
                            new ItemRequest("3010091676-1132351437", "1132351437", 3, "Pantalón Levi´s", "Compra en línea"),
                            new ItemRequest("3010091676-1179743767", "1179743767", 1, "Camisa Polo", "Compra en línea")
                    ),
                    "L  SANTA FE"
            );

            PedidoResponse expectedPedido = new PedidoResponse(
                    "3010091676", "75c97531-abf5-4524-8107-90aa48d08efc",
                    "online", "2025-12-06", false, false,
                    List.of("3010091676-1132351437", "3010091676-1179743767"),
                    "L  SANTA FE", "10"
            );

            when(pedidoApiPort.createPedido(any(PedidoResponse.class))).thenReturn(expectedPedido);
            when(itemApiPort.createItem(any(ItemResponse.class))).thenReturn(
                    new ItemResponse("3010091676-1132351437", "1132351437", 3, "Pantalón Levi´s", "Compra en línea", "1")
            );

            PedidoResponse result = orderDetailService.createOrderDetail(request);

            assertNotNull(result);
            assertEquals("3010091676", result.orderRef());
            assertEquals("75c97531-abf5-4524-8107-90aa48d08efc", result.userId());
            assertEquals("online", result.canal());
            assertEquals("L  SANTA FE", result.storeName());
            assertEquals(2, result.items().size());

            verify(pedidoApiPort).createPedido(any(PedidoResponse.class));
            verify(itemApiPort, times(2)).createItem(any(ItemResponse.class));
        }

        @Test
        @DisplayName("should propagate exception when pedido creation fails")
        void shouldPropagateExceptionWhenPedidoCreationFails() {
            OrderDetailRequest request = new OrderDetailRequest(
                    "3010091676", "75c97531-abf5-4524-8107-90aa48d08efc",
                    "online", "2025-12-06", false, false,
                    List.of(new ItemRequest("3010091676-1132351437", "1132351437", 3, "Pantalón Levi´s", "Compra en línea")),
                    "L  SANTA FE"
            );

            when(pedidoApiPort.createPedido(any(PedidoResponse.class)))
                    .thenThrow(new RuntimeException("API error"));

            assertThrows(RuntimeException.class,
                    () -> orderDetailService.createOrderDetail(request));

            verify(pedidoApiPort).createPedido(any(PedidoResponse.class));
            verifyNoInteractions(itemApiPort);
        }
    }

    @Nested
    @DisplayName("updateOrderDetail")
    class UpdateOrderDetail {

        @Test
        @DisplayName("should update pedido and re-create items successfully")
        void shouldUpdatePedidoAndItems() {
            PedidoResponse existingPedido = new PedidoResponse(
                    "3010091676", "75c97531-abf5-4524-8107-90aa48d08efc",
                    "online", "2025-12-06", false, false,
                    List.of("3010091676-1132351437"),
                    "L  SANTA FE", "10"
            );

            OrderDetailRequest request = new OrderDetailRequest(
                    "3010091676", "75c97531-abf5-4524-8107-90aa48d08efc",
                    "online", "2025-12-07", false, false,
                    List.of(new ItemRequest("3010091676-1132351437", "1132351437", 5, "Pantalón Levi´s", "Compra en línea")),
                    "L  SANTA FE"
            );

            PedidoResponse updatedPedido = new PedidoResponse(
                    "3010091676", "75c97531-abf5-4524-8107-90aa48d08efc",
                    "online", "2025-12-07", false, false,
                    List.of("3010091676-1132351437"),
                    "L  SANTA FE", "10"
            );

            when(pedidoApiPort.findPedidoByOrderRef("3010091676")).thenReturn(List.of(existingPedido));
            when(pedidoApiPort.updatePedido(eq("10"), any(PedidoResponse.class))).thenReturn(updatedPedido);
            when(itemApiPort.createItem(any(ItemResponse.class))).thenReturn(
                    new ItemResponse("3010091676-1132351437", "1132351437", 5, "Pantalón Levi´s", "Compra en línea", "1")
            );

            PedidoResponse result = orderDetailService.updateOrderDetail("3010091676", request);

            assertNotNull(result);
            assertEquals("3010091676", result.orderRef());
            assertEquals("2025-12-07", result.orderStatus());

            verify(pedidoApiPort).findPedidoByOrderRef("3010091676");
            verify(pedidoApiPort).updatePedido(eq("10"), any(PedidoResponse.class));
            verify(itemApiPort).createItem(any(ItemResponse.class));
        }

        @Test
        @DisplayName("should throw OrderNotFoundException when order does not exist")
        void shouldThrowWhenOrderNotFound() {
            OrderDetailRequest request = new OrderDetailRequest(
                    "INVALID", "user-1", "online", "2025-12-06", false, false,
                    List.of(new ItemRequest("INVALID-123", "123", 1, "Product", "Status")),
                    "Store"
            );

            when(pedidoApiPort.findPedidoByOrderRef("INVALID")).thenReturn(Collections.emptyList());

            assertThrows(OrderNotFoundException.class,
                    () -> orderDetailService.updateOrderDetail("INVALID", request));

            verify(pedidoApiPort).findPedidoByOrderRef("INVALID");
            verify(pedidoApiPort, never()).updatePedido(any(), any());
            verifyNoInteractions(itemApiPort);
        }

        @Test
        @DisplayName("should throw OrderNotFoundException when pedido list is null for update")
        void shouldThrowWhenPedidoNullForUpdate() {
            OrderDetailRequest request = new OrderDetailRequest(
                    "INVALID", "user-1", "online", "2025-12-06", false, false,
                    List.of(new ItemRequest("INVALID-123", "123", 1, "Product", "Status")),
                    "Store"
            );

            when(pedidoApiPort.findPedidoByOrderRef("INVALID")).thenReturn(null);

            assertThrows(OrderNotFoundException.class,
                    () -> orderDetailService.updateOrderDetail("INVALID", request));
        }
    }

    @Nested
    @DisplayName("searchOrders")
    class SearchOrders {

        private PedidoResponse pedido1;
        private PedidoResponse pedido2;
        private ItemResponse item1;
        private ItemResponse item2;
        private ItemResponse item3;

        @BeforeEach
        void setUp() {
            pedido1 = new PedidoResponse(
                    "3010091676", "user-1", "online", "2025-12-06",
                    false, false,
                    List.of("3010091676-1132351437", "3010091676-1179743767"),
                    "L  SANTA FE", "1"
            );
            pedido2 = new PedidoResponse(
                    "3010091677", "user-2", "physical", "2025-12-08",
                    false, false,
                    List.of("3010091677-999"),
                    "Liverpool Galerías Toluca", "2"
            );

            item1 = new ItemResponse("3010091676-1132351437", "1132351437", 3, "Pantalón Levi's", "Compra en línea", "1");
            item2 = new ItemResponse("3010091676-1179743767", "1179743767", 1, "Laptop Lenovo thinkpad", "Pedido entregado", "2");
            item3 = new ItemResponse("3010091677-999", "999", 2, "Camisa Polo Ralph Lauren", "Compra en línea", "3");
        }

        @Test
        @DisplayName("should filter orders by storeName")
        void shouldFilterByStoreName() {
            when(pedidoApiPort.findAllPedidos()).thenReturn(List.of(pedido1, pedido2));
            when(itemApiPort.findAllItems()).thenReturn(List.of(item1, item2, item3));

            List<OrderSearchResponse> result = orderDetailService.searchOrders("santa fe");

            assertEquals(1, result.size());
            assertEquals("3010091676", result.getFirst().orderRef());
            assertEquals("L  SANTA FE", result.getFirst().storeName());
            assertEquals(2, result.getFirst().items().size());
        }

        @Test
        @DisplayName("should filter orders by orderRef")
        void shouldFilterByOrderRef() {
            when(pedidoApiPort.findAllPedidos()).thenReturn(List.of(pedido1, pedido2));
            when(itemApiPort.findAllItems()).thenReturn(List.of(item1, item2, item3));

            List<OrderSearchResponse> result = orderDetailService.searchOrders("3010091677");

            assertEquals(2, result.size());
            assertEquals("3010091676", result.getFirst().orderRef());
        }

        @Test
        @DisplayName("should filter orders by orderStatus")
        void shouldFilterByOrderStatus() {
            when(pedidoApiPort.findAllPedidos()).thenReturn(List.of(pedido1, pedido2));
            when(itemApiPort.findAllItems()).thenReturn(List.of(item1, item2, item3));

            List<OrderSearchResponse> result = orderDetailService.searchOrders("2025-12-08");

            assertEquals(2, result.size());
            assertEquals("3010091676", result.getFirst().orderRef());
        }

        @Test
        @DisplayName("should filter orders by item displayName")
        void shouldFilterByItemDisplayName() {
            when(pedidoApiPort.findAllPedidos()).thenReturn(List.of(pedido1, pedido2));
            when(itemApiPort.findAllItems()).thenReturn(List.of(item1, item2, item3));

            List<OrderSearchResponse> result = orderDetailService.searchOrders("laptop");

            assertEquals(1, result.size());
            assertEquals("3010091676", result.getFirst().orderRef());
        }

        @Test
        @DisplayName("should be case insensitive")
        void shouldBeCaseInsensitive() {
            when(pedidoApiPort.findAllPedidos()).thenReturn(List.of(pedido1, pedido2));
            when(itemApiPort.findAllItems()).thenReturn(List.of(item1, item2, item3));

            List<OrderSearchResponse> result = orderDetailService.searchOrders("SANTA FE");

            assertEquals(1, result.size());
            assertEquals("3010091676", result.getFirst().orderRef());
        }

        @Test
        @DisplayName("should ignore accents in search")
        void shouldIgnoreAccents() {
            when(pedidoApiPort.findAllPedidos()).thenReturn(List.of(pedido1, pedido2));
            when(itemApiPort.findAllItems()).thenReturn(List.of(item1, item2, item3));

            List<OrderSearchResponse> result = orderDetailService.searchOrders("galerias");

            assertEquals(1, result.size());
            assertEquals("3010091677", result.getFirst().orderRef());
        }

        @Test
        @DisplayName("should return empty list when no match")
        void shouldReturnEmptyWhenNoMatch() {
            when(pedidoApiPort.findAllPedidos()).thenReturn(List.of(pedido1, pedido2));
            when(itemApiPort.findAllItems()).thenReturn(List.of(item1, item2, item3));

            List<OrderSearchResponse> result = orderDetailService.searchOrders("zzzznonexistent");

            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("should return all orders when query is empty")
        void shouldReturnAllWhenQueryEmpty() {
            when(pedidoApiPort.findAllPedidos()).thenReturn(List.of(pedido1, pedido2));
            when(itemApiPort.findAllItems()).thenReturn(List.of(item1, item2, item3));

            List<OrderSearchResponse> result = orderDetailService.searchOrders("");

            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("should include item details in response")
        void shouldIncludeItemDetails() {
            when(pedidoApiPort.findAllPedidos()).thenReturn(List.of(pedido1));
            when(itemApiPort.findAllItems()).thenReturn(List.of(item1, item2));

            List<OrderSearchResponse> result = orderDetailService.searchOrders("santa");

            assertEquals(1, result.size());
            OrderSearchResponse order = result.getFirst();
            assertEquals(2, order.items().size());
            assertEquals("Pantalón Levi's", order.items().get(0).displayName());
            assertEquals("Laptop Lenovo thinkpad", order.items().get(1).displayName());
        }

        @Test
        @DisplayName("should handle pedido with null items list")
        void shouldHandlePedidoWithNullItems() {
            PedidoResponse pedidoNoItems = new PedidoResponse(
                    "3010091678", "user-3", "online", "2025-12-06",
                    false, false, null, "SANTA FE Store", "3"
            );

            when(pedidoApiPort.findAllPedidos()).thenReturn(List.of(pedidoNoItems));
            when(itemApiPort.findAllItems()).thenReturn(List.of());

            List<OrderSearchResponse> result = orderDetailService.searchOrders("santa");

            assertEquals(1, result.size());
            assertTrue(result.getFirst().items().isEmpty());
        }
    }
}
