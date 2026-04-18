package com.retail.management.order.application.service;

import com.retail.management.order.application.dto.ItemRequest;
import com.retail.management.order.application.dto.OrderDetailRequest;
import com.retail.management.order.domain.exception.OrderNotFoundException;
import com.retail.management.order.domain.model.OrderDetail;
import com.retail.management.order.domain.port.in.OrderDetailServicePort;
import com.retail.management.order.domain.port.out.ItemApiPort;
import com.retail.management.order.domain.port.out.PedidoApiPort;
import com.retail.management.order.infrastructure.rest.dto.ItemResponse;
import com.retail.management.order.infrastructure.rest.dto.PedidoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailService implements OrderDetailServicePort {

    private static final Logger log = LoggerFactory.getLogger(OrderDetailService.class);

    private final PedidoApiPort pedidoApiPort;
    private final ItemApiPort itemApiPort;

    public OrderDetailService(PedidoApiPort pedidoApiPort, ItemApiPort itemApiPort) {
        this.pedidoApiPort = pedidoApiPort;
        this.itemApiPort = itemApiPort;
    }

    @Override
    public List<OrderDetail> getOrderDetailByOrderRef(String orderRef) {
        List<PedidoResponse> pedidos = pedidoApiPort.findPedidoByOrderRef(orderRef);

        if (pedidos == null || pedidos.isEmpty()) {
            throw new OrderNotFoundException(orderRef);
        }

        PedidoResponse pedido = pedidos.getFirst();
        String canal = pedido.canal();
        String orderStatus = pedido.orderStatus();

        List<OrderDetail> orderDetails = new ArrayList<>();

        if (pedido.items() != null) {
            for (String itemId : pedido.items()) {
                List<ItemResponse> items = itemApiPort.findItemByItemId(itemId);

                if (items != null && !items.isEmpty()) {
                    ItemResponse item = items.getFirst();
                    OrderDetail detail = new OrderDetail(
                            item.itemId(),
                            item.quantity(),
                            item.displayName(),
                            canal,
                            orderStatus
                    );
                    orderDetails.add(detail);
                } else {
                    log.warn("Item not found for itemId={}", itemId);
                }
            }
        }

        return orderDetails;
    }

    @Override
    public PedidoResponse createOrderDetail(OrderDetailRequest request) {
        List<String> itemIds = request.items().stream()
                .map(ItemRequest::itemId)
                .toList();

        PedidoResponse pedido = new PedidoResponse(
                request.orderRef(),
                request.userId(),
                request.canal(),
                request.orderStatus(),
                request.marketPlace(),
                request.giftRegistry(),
                itemIds,
                request.storeName(),
                null
        );

        PedidoResponse createdPedido = pedidoApiPort.createPedido(pedido);

        for (ItemRequest itemRequest : request.items()) {
            ItemResponse itemBody = new ItemResponse(
                    itemRequest.itemId(),
                    itemRequest.skuId(),
                    itemRequest.quantity(),
                    itemRequest.displayName(),
                    itemRequest.deliveryStatus(),
                    null
            );
            itemApiPort.createItem(itemBody);
        }

        return createdPedido;
    }

    @Override
    public PedidoResponse updateOrderDetail(String orderRef, OrderDetailRequest request) {
        List<PedidoResponse> pedidos = pedidoApiPort.findPedidoByOrderRef(orderRef);

        if (pedidos == null || pedidos.isEmpty()) {
            throw new OrderNotFoundException(orderRef);
        }

        PedidoResponse existingPedido = pedidos.getFirst();

        List<String> itemIds = request.items().stream()
                .map(ItemRequest::itemId)
                .toList();

        PedidoResponse updatedPedido = new PedidoResponse(
                request.orderRef(),
                request.userId(),
                request.canal(),
                request.orderStatus(),
                request.marketPlace(),
                request.giftRegistry(),
                itemIds,
                request.storeName(),
                existingPedido.id()
        );

        PedidoResponse result = pedidoApiPort.updatePedido(existingPedido.id(), updatedPedido);

        List<String> oldItems =existingPedido.items();
        if (oldItems != null && oldItems.size() > 0) {
            for (String itemId : oldItems) {
                List<ItemResponse> items = itemApiPort.findItemByItemId(itemId);
                if (items != null && !items.isEmpty()) {
                    ItemResponse item = items.getFirst();
                    itemApiPort.deleteItem(item.id());
                }
            }
        }

        for (ItemRequest itemRequest : request.items()) {
            ItemResponse itemBody = new ItemResponse(
                    itemRequest.itemId(),
                    itemRequest.skuId(),
                    itemRequest.quantity(),
                    itemRequest.displayName(),
                    itemRequest.deliveryStatus(),
                    null
            );
            itemApiPort.createItem(itemBody);
        }

        return result;
    }
}
