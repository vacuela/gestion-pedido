package com.retail.management.order.application.mapper;

import com.retail.management.order.application.dto.DeliveryDataRequest;
import com.retail.management.order.application.dto.DeliveryDataResponse;
import com.retail.management.order.domain.model.DeliveryData;
import org.springframework.stereotype.Component;

@Component
public class DeliveryDataMapper {

    public DeliveryData toDomain(DeliveryDataRequest request) {
        DeliveryData deliveryData = new DeliveryData();
        deliveryData.setDireccionEnvio(request.direccionEnvio());
        return deliveryData;
    }

    public DeliveryDataResponse toResponse(DeliveryData deliveryData) {
        return new DeliveryDataResponse(
                deliveryData.getId(),
                deliveryData.getDireccionEnvio()
        );
    }
}
