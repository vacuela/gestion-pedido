package com.retail.management.order.infrastructure.rest.adapter;

import com.retail.management.order.domain.port.out.OrderApiPort;
import com.retail.management.order.infrastructure.rest.dto.PedidoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@Component
public class OrderApiAdapter implements OrderApiPort {

    private static final Logger log = LoggerFactory.getLogger(OrderApiAdapter.class);

    private final RestClient restClient;

    public OrderApiAdapter(RestClient orderRestClient) {
        this.restClient = orderRestClient;
    }

    @Override
    public List<String> findOrderRefsByUserId(String userId) {
        try {
            List<PedidoResponse> pedidos = restClient.get()
                    .uri("/api/v1/pedidos?userId={userId}", userId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            if (pedidos == null) {
                return Collections.emptyList();
            }

            return pedidos.stream()
                    .map(PedidoResponse::orderRef)
                    .toList();
        } catch (Exception e) {
            log.error("Error fetching orders for userId={}: {}", userId, e.getMessage());
            return Collections.emptyList();
        }
    }
}
