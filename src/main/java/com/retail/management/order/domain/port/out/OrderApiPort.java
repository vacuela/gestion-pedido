package com.retail.management.order.domain.port.out;

import java.util.List;

public interface OrderApiPort {

    List<String> findOrderRefsByUserId(String userId);
}
