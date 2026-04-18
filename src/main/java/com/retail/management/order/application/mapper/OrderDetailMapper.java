package com.retail.management.order.application.mapper;

import com.retail.management.order.application.dto.OrderDetailResponse;
import com.retail.management.order.domain.model.OrderDetail;
import org.springframework.stereotype.Component;

@Component
public class OrderDetailMapper {

    public OrderDetailResponse toResponse(OrderDetail orderDetail) {
        return new OrderDetailResponse(
                orderDetail.getItemId(),
                orderDetail.getQuantity(),
                orderDetail.getDisplayName(),
                orderDetail.getCanal(),
                orderDetail.getOrderStatus()
        );
    }
}
