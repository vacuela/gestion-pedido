package com.retail.management.order.infrastructure.persistence.mongodb.mapper;

import com.retail.management.order.domain.model.Customer;
import com.retail.management.order.domain.model.DeliveryData;
import com.retail.management.order.infrastructure.persistence.mongodb.document.CustomerDocument;
import com.retail.management.order.infrastructure.persistence.mongodb.document.DeliveryDataDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDocumentMapperDeliveryDataTest {

    private CustomerDocumentMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CustomerDocumentMapper();
    }

    @Test
    @DisplayName("should map Customer with delivery data to CustomerDocument")
    void shouldMapToDocumentWithDeliveryData() {
        Customer customer = new Customer("1", "Juan", "Pérez", "López", "juan@example.com");
        DeliveryData dd = new DeliveryData("dd-001", "Av. Reforma 123");
        customer.setDatosEntrega(List.of(dd));

        CustomerDocument result = mapper.toDocument(customer);

        assertEquals(1, result.getDatosEntrega().size());
        assertEquals("dd-001", result.getDatosEntrega().get(0).getId());
        assertEquals("Av. Reforma 123", result.getDatosEntrega().get(0).getDireccionEnvio());
    }

    @Test
    @DisplayName("should map CustomerDocument with delivery data to Customer")
    void shouldMapToDomainWithDeliveryData() {
        CustomerDocument document = new CustomerDocument("1", "Juan", "Pérez", "López", "juan@example.com");
        DeliveryDataDocument ddDoc = new DeliveryDataDocument("dd-001", "Av. Reforma 123");
        document.setDatosEntrega(List.of(ddDoc));

        Customer result = mapper.toDomain(document);

        assertEquals(1, result.getDatosEntrega().size());
        assertEquals("dd-001", result.getDatosEntrega().get(0).getId());
        assertEquals("Av. Reforma 123", result.getDatosEntrega().get(0).getDireccionEnvio());
    }

    @Test
    @DisplayName("should handle null delivery data when mapping to document")
    void shouldHandleNullDeliveryDataToDocument() {
        Customer customer = new Customer("1", "Juan", "Pérez", "López", "juan@example.com");
        customer.setDatosEntrega(null);

        CustomerDocument result = mapper.toDocument(customer);

        assertTrue(result.getDatosEntrega().isEmpty());
    }

    @Test
    @DisplayName("should handle null delivery data when mapping to domain")
    void shouldHandleNullDeliveryDataToDomain() {
        CustomerDocument document = new CustomerDocument("1", "Juan", "Pérez", "López", "juan@example.com");
        document.setDatosEntrega(null);

        Customer result = mapper.toDomain(document);

        assertNotNull(result.getDatosEntrega());
    }
}
