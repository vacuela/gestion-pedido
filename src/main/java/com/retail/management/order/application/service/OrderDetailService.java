package com.retail.management.order.application.service;

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
}
