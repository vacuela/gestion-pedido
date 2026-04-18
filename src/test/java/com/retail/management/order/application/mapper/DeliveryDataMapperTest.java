package com.retail.management.order.application.mapper;

import com.retail.management.order.application.dto.DeliveryDataRequest;
import com.retail.management.order.application.dto.DeliveryDataResponse;
import com.retail.management.order.domain.model.DeliveryData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeliveryDataMapperTest {

    private DeliveryDataMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new DeliveryDataMapper();
    }

    @Test
    @DisplayName("should map DeliveryDataRequest to domain DeliveryData")
    void shouldMapToDomain() {
        DeliveryDataRequest request = new DeliveryDataRequest("Av. Reforma 123, Col. Centro, CDMX");

        DeliveryData result = mapper.toDomain(request);

        assertNull(result.getId());
        assertEquals("Av. Reforma 123, Col. Centro, CDMX", result.getDireccionEnvio());
    }

    @Test
    @DisplayName("should map domain DeliveryData to DeliveryDataResponse")
    void shouldMapToResponse() {
        DeliveryData deliveryData = new DeliveryData("dd-001", "Av. Reforma 123, Col. Centro, CDMX");

        DeliveryDataResponse result = mapper.toResponse(deliveryData);

        assertEquals("dd-001", result.id());
        assertEquals("Av. Reforma 123, Col. Centro, CDMX", result.direccionEnvio());
    }
}
