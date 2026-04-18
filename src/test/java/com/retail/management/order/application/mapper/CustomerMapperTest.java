package com.retail.management.order.application.mapper;

import com.retail.management.order.application.dto.CustomerRequest;
import com.retail.management.order.application.dto.CustomerResponse;
import com.retail.management.order.domain.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {

    private CustomerMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CustomerMapper();
    }

    @Test
    @DisplayName("should map CustomerRequest to domain Customer")
    void shouldMapToDomain() {
        CustomerRequest request = new CustomerRequest("32761236886213", "Juan", "Pérez", "López", "juan@example.com");

        Customer result = mapper.toDomain(request);

        assertNotNull(result.getUserId());
        assertEquals("Juan", result.getNombre());
        assertEquals("Pérez", result.getApellidoPaterno());
        assertEquals("López", result.getApellidoMaterno());
        assertEquals("juan@example.com", result.getEmail());
    }

    @Test
    @DisplayName("should map domain Customer to CustomerResponse with empty orders")
    void shouldMapToResponse() {
        Customer customer = new Customer("32761236886213", "Juan", "Pérez", "López", "juan@example.com");

        CustomerResponse result = mapper.toResponse(customer);

        assertEquals("32761236886213", result.userId());
        assertEquals("Juan", result.nombre());
        assertEquals("Pérez", result.apellidoPaterno());
        assertEquals("López", result.apellidoMaterno());
        assertEquals("juan@example.com", result.email());
        assertEquals(Collections.emptyList(), result.orders());
    }

    @Test
    @DisplayName("should map domain Customer to CustomerResponse with orders")
    void shouldMapToResponseWithOrders() {
        Customer customer = new Customer("32761236886213", "Juan", "Pérez", "López", "juan@example.com");
        List<String> orders = List.of("20251216366900020031", "20251216366900020032");

        CustomerResponse result = mapper.toResponse(customer, orders);

        assertEquals("32761236886213", result.userId());
        assertEquals("Juan", result.nombre());
        assertEquals("Pérez", result.apellidoPaterno());
        assertEquals("López", result.apellidoMaterno());
        assertEquals("juan@example.com", result.email());
        assertEquals(orders, result.orders());
    }
}
