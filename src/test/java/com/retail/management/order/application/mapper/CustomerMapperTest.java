package com.retail.management.order.application.mapper;

import com.retail.management.order.application.dto.CustomerRequest;
import com.retail.management.order.application.dto.CustomerResponse;
import com.retail.management.order.domain.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        CustomerRequest request = new CustomerRequest("Juan", "Pérez", "López", "juan@example.com");

        Customer result = mapper.toDomain(request);

        assertNull(result.getId());
        assertEquals("Juan", result.getNombre());
        assertEquals("Pérez", result.getApellidoPaterno());
        assertEquals("López", result.getApellidoMaterno());
        assertEquals("juan@example.com", result.getEmail());
    }

    @Test
    @DisplayName("should map domain Customer to CustomerResponse")
    void shouldMapToResponse() {
        Customer customer = new Customer("1", "Juan", "Pérez", "López", "juan@example.com");

        CustomerResponse result = mapper.toResponse(customer);

        assertEquals("1", result.id());
        assertEquals("Juan", result.nombre());
        assertEquals("Pérez", result.apellidoPaterno());
        assertEquals("López", result.apellidoMaterno());
        assertEquals("juan@example.com", result.email());
    }
}
