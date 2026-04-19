package com.retail.management.order.infrastructure.rest.adapter;

import com.retail.management.order.domain.exception.ExternalApiException;
import com.retail.management.order.domain.port.out.ItemApiPort;
import com.retail.management.order.infrastructure.rest.dto.ItemResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class ItemApiAdapter implements ItemApiPort {

    private static final Logger log = LoggerFactory.getLogger(ItemApiAdapter.class);

    private final RestClient restClient;

    public ItemApiAdapter(RestClient orderRestClient) {
        this.restClient = orderRestClient;
    }

    @Override
    public List<ItemResponse> findAllItems() {
        try {
            List<ItemResponse> items = restClient.get()
                    .uri("/api/v1/items")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            return items != null ? items : List.of();
        } catch (Exception e) {
            log.error("Error fetching all items: {}", e.getMessage());
            throw new ExternalApiException("Error fetching all items", e);
        }
    }

    @Override
    public List<ItemResponse> findItemByItemId(String itemId) {
        try {
            List<ItemResponse> items = restClient.get()
                    .uri("/api/v1/items?itemId={itemId}", itemId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            return items != null ? items : List.of();
        } catch (Exception e) {
            log.error("Error fetching item for itemId={}: {}", itemId, e.getMessage());
            throw new ExternalApiException("Error fetching item data for itemId: " + itemId, e);
        }
    }

    @Override
    public ItemResponse createItem(ItemResponse item) {
        try {
            return restClient.post()
                    .uri("/api/v1/items")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(item)
                    .retrieve()
                    .body(ItemResponse.class);
        } catch (Exception e) {
            log.error("Error creating item for itemId={}: {}", item.itemId(), e.getMessage());
            throw new ExternalApiException("Error creating item for itemId: " + item.itemId(), e);
        }
    }

    @Override
    public ItemResponse deleteItem(String id) {
        try {
            return restClient.delete()
                    .uri("/api/v1/items/{id}", id)
                    .retrieve()
                    .body(ItemResponse.class);
        } catch (Exception e) {
            log.error("Error deleting item for id={}: {}", id, e.getMessage());
            throw new ExternalApiException("Error deleting item for id: " + id, e);
        }
    }
}
