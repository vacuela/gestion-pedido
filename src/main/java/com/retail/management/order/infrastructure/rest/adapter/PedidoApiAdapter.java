package com.retail.management.order.infrastructure.rest.adapter;

import com.retail.management.order.domain.exception.ExternalApiException;
import com.retail.management.order.domain.port.out.PedidoApiPort;
import com.retail.management.order.infrastructure.rest.dto.PedidoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
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
    public List<PedidoResponse> findAllPedidos() {
        try {
            List<PedidoResponse> pedidos = restClient.get()
                    .uri("/api/v1/pedidos")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            return pedidos != null ? pedidos : List.of();
        } catch (Exception e) {
            log.error("Error fetching all pedidos: {}", e.getMessage());
            throw new ExternalApiException("Error fetching all orders", e);
        }
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

    @Override
    public PedidoResponse createPedido(PedidoResponse pedido) {
        try {
            return restClient.post()
                    .uri("/api/v1/pedidos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(pedido)
                    .retrieve()
                    .body(PedidoResponse.class);
        } catch (Exception e) {
            log.error("Error creating pedido for orderRef={}: {}", pedido.orderRef(), e.getMessage());
            throw new ExternalApiException("Error creating order for orderRef: " + pedido.orderRef(), e);
        }
    }

    @Override
    public PedidoResponse updatePedido(String id, PedidoResponse pedido) {
        try {
            return restClient.put()
                    .uri("/api/v1/pedidos/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(pedido)
                    .retrieve()
                    .body(PedidoResponse.class);
        } catch (Exception e) {
            log.error("Error updating pedido id={}: {}", id, e.getMessage());
            throw new ExternalApiException("Error updating order with id: " + id, e);
        }
    }

    @Override
    public PedidoResponse deletePedido(String id) {
        try {
            return restClient.delete()
                    .uri("/api/v1/pedidos/{id}", id)
                    .retrieve()
                    .body(PedidoResponse.class);
        } catch (Exception e) {
            log.error("Error deleting pedido id={}: {}", id, e.getMessage());
            throw new ExternalApiException("Error deleting order with id: " + id, e);
        }
    }   
}
