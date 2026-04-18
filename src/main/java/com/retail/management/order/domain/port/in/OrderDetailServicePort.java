package com.retail.management.order.domain.port.in;

import com.retail.management.order.application.dto.OrderDetailRequest;
import com.retail.management.order.domain.model.OrderDetail;
import com.retail.management.order.infrastructure.rest.dto.PedidoResponse;

import java.util.List;

public interface OrderDetailServicePort {

    List<OrderDetail> getOrderDetailByOrderRef(String orderRef);

    PedidoResponse createOrderDetail(OrderDetailRequest request);

    PedidoResponse updateOrderDetail(String orderRef, OrderDetailRequest request);
}
