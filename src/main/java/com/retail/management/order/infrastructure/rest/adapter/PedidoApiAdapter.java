package com.retail.management.order.infrastructure.rest.adapter;

import com.retail.management.order.domain.exception.ExternalApiException;
import com.retail.management.order.domain.port.out.PedidoApiPort;
import com.retail.management.order.infrastructure.rest.dto.PedidoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class PedidoApiAdapter implements PedidoApiPort {

    private static final Logger log = LoggerFactory.getLogger(PedidoApiAdapter.class);

    private final RestClient restClient;

    public PedidoApiAdapter(RestClient orderRestClient) {
        this.restClient = orderRestClient;
    }

    @Override
    public List<PedidoResponse> findPedidoByOrderRef(String orderRef) {
        try {
            List<PedidoResponse> pedidos = restClient.get()
                    .uri("/api/v1/pedidos?orderRef={orderRef}", orderRef)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            return pedidos != null ? pedidos : List.of();
        } catch (Exception e) {
            log.error("Error fetching pedido for orderRef={}: {}", orderRef, e.getMessage());
            throw new ExternalApiException("Error fetching order data for orderRef: " + orderRef, e);
        }
    }
}
