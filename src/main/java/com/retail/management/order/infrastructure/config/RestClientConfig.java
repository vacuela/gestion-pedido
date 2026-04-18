package com.retail.management.order.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${order-api.base-url}")
    private String orderApiBaseUrl;

    @Bean
    public RestClient orderRestClient() {
        return RestClient.builder()
                .baseUrl(orderApiBaseUrl)
                .build();
    }
}
