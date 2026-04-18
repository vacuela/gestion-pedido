package com.retail.management.order.infrastructure.rest.adapter;

import com.retail.management.order.domain.exception.ExternalApiException;
import com.retail.management.order.domain.port.out.ItemApiPort;
import com.retail.management.order.infrastructure.rest.dto.ItemResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
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
}
