package com.retail.management.order.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order Management - Customer API")
                        .version("1.0.0")
                        .description("REST API for customer management (CRUD)")
                        .contact(new Contact()
                                .name("Retail Management")
                                .email("support@retailmanagement.com")));
    }
}
