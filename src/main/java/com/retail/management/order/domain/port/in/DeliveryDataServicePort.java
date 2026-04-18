package com.retail.management.order.domain.port.in;

import com.retail.management.order.domain.model.DeliveryData;

import java.util.List;

public interface DeliveryDataServicePort {

    DeliveryData addDeliveryData(String customerId, DeliveryData deliveryData);

    List<DeliveryData> findAllByCustomerId(String customerId);

    DeliveryData findByCustomerIdAndDeliveryDataId(String customerId, String deliveryDataId);

    DeliveryData updateDeliveryData(String customerId, String deliveryDataId, DeliveryData deliveryData);

    void deleteDeliveryData(String customerId, String deliveryDataId);
}
