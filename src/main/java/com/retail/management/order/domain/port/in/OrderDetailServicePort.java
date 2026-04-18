package com.retail.management.order.domain.port.in;

import com.retail.management.order.domain.model.OrderDetail;

import java.util.List;

public interface OrderDetailServicePort {

    List<OrderDetail> getOrderDetailByOrderRef(String orderRef);
}
