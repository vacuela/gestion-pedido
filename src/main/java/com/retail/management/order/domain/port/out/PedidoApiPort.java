package com.retail.management.order.domain.port.out;

import com.retail.management.order.infrastructure.rest.dto.PedidoResponse;

import java.util.List;

public interface PedidoApiPort {

    List<PedidoResponse> findPedidoByOrderRef(String orderRef);

    PedidoResponse createPedido(PedidoResponse pedido);

    PedidoResponse updatePedido(String id, PedidoResponse pedido);
}
