package com.retail.management.order.infrastructure.persistence.mongodb.mapper;

import com.retail.management.order.domain.model.Customer;
import com.retail.management.order.infrastructure.persistence.mongodb.document.CustomerDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDocumentMapperTest {

    private CustomerDocumentMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CustomerDocumentMapper();
    }

    @Test
    @DisplayName("should map domain Customer to CustomerDocument")
    void shouldMapToDocument() {
        Customer customer = new Customer("1", "Juan", "Pérez", "López", "juan@example.com");

        CustomerDocument result = mapper.toDocument(customer);

        assertEquals("1", result.getUserId());
        assertEquals("Juan", result.getNombre());
        assertEquals("Pérez", result.getApellidoPaterno());
        assertEquals("López", result.getApellidoMaterno());
        assertEquals("juan@example.com", result.getEmail());
    }

    @Test
    @DisplayName("should map CustomerDocument to domain Customer")
    void shouldMapToDomain() {
        CustomerDocument document = new CustomerDocument("1", "Juan", "Pérez", "López", "juan@example.com");

        Customer result = mapper.toDomain(document);

        assertEquals("1", result.getUserId());
        assertEquals("Juan", result.getNombre());
        assertEquals("Pérez", result.getApellidoPaterno());
        assertEquals("López", result.getApellidoMaterno());
        assertEquals("juan@example.com", result.getEmail());
    }
}
