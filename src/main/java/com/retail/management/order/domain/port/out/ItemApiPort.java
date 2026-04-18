package com.retail.management.order.domain.port.out;

import com.retail.management.order.infrastructure.rest.dto.ItemResponse;

import java.util.List;

public interface ItemApiPort {

    List<ItemResponse> findItemByItemId(String itemId);
}
