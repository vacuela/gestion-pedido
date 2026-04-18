package com.retail.management.order.infrastructure.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.management.order.application.dto.DeliveryDataRequest;
import com.retail.management.order.application.dto.DeliveryDataResponse;
import com.retail.management.order.application.mapper.DeliveryDataMapper;
import com.retail.management.order.domain.exception.CustomerNotFoundException;
import com.retail.management.order.domain.exception.DeliveryDataNotFoundException;
import com.retail.management.order.domain.model.DeliveryData;
import com.retail.management.order.domain.port.in.DeliveryDataServicePort;
import com.retail.management.order.infrastructure.rest.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DeliveryDataControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private DeliveryDataServicePort deliveryDataService;

    @Mock
    private DeliveryDataMapper deliveryDataMapper;

    @InjectMocks
    private DeliveryDataController deliveryDataController;

    private DeliveryData deliveryData;
    private DeliveryDataResponse response;
    private DeliveryDataRequest request;

    private static final String BASE_URL = "/api/v1/customers/cust-1/delivery-data";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(deliveryDataController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        deliveryData = new DeliveryData("dd-001", "Av. Reforma 123, Col. Centro, CDMX");
        response = new DeliveryDataResponse("dd-001", "Av. Reforma 123, Col. Centro, CDMX");
        request = new DeliveryDataRequest("Av. Reforma 123, Col. Centro, CDMX");
    }

    @Nested
    @DisplayName("POST /api/v1/customers/{customerId}/delivery-data")
    class AddDeliveryData {

        @Test
        @DisplayName("should add delivery data and return 201")
        void shouldAddDeliveryData() throws Exception {
            when(deliveryDataMapper.toDomain(any(DeliveryDataRequest.class))).thenReturn(deliveryData);
            when(deliveryDataService.addDeliveryData(eq("cust-1"), any(DeliveryData.class))).thenReturn(deliveryData);
            when(deliveryDataMapper.toResponse(deliveryData)).thenReturn(response);

            mockMvc.perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value("dd-001"))
                    .andExpect(jsonPath("$.direccionEnvio").value("Av. Reforma 123, Col. Centro, CDMX"));
        }

        @Test
        @DisplayName("should return 400 when validation fails")
        void shouldReturn400WhenInvalid() throws Exception {
            DeliveryDataRequest invalid = new DeliveryDataRequest("");

            mockMvc.perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalid)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400));
        }

        @Test
        @DisplayName("should return 404 when customer not found")
        void shouldReturn404WhenCustomerNotFound() throws Exception {
            when(deliveryDataMapper.toDomain(any(DeliveryDataRequest.class))).thenReturn(deliveryData);
            when(deliveryDataService.addDeliveryData(eq("cust-1"), any(DeliveryData.class)))
                    .thenThrow(new CustomerNotFoundException("cust-1"));

            mockMvc.perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/customers/{customerId}/delivery-data")
    class GetAllDeliveryData {

        @Test
        @DisplayName("should return all delivery data")
        void shouldReturnAll() throws Exception {
            DeliveryData dd2 = new DeliveryData("dd-002", "Calle Juárez 456");
            DeliveryDataResponse response2 = new DeliveryDataResponse("dd-002", "Calle Juárez 456");

            when(deliveryDataService.findAllByCustomerId("cust-1")).thenReturn(List.of(deliveryData, dd2));
            when(deliveryDataMapper.toResponse(deliveryData)).thenReturn(response);
            when(deliveryDataMapper.toResponse(dd2)).thenReturn(response2);

            mockMvc.perform(get(BASE_URL))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2));
        }

        @Test
        @DisplayName("should return 404 when customer not found")
        void shouldReturn404WhenCustomerNotFound() throws Exception {
            when(deliveryDataService.findAllByCustomerId("cust-1"))
                    .thenThrow(new CustomerNotFoundException("cust-1"));

            mockMvc.perform(get(BASE_URL))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/customers/{customerId}/delivery-data/{deliveryDataId}")
    class GetDeliveryDataById {

        @Test
        @DisplayName("should return delivery data when found")
        void shouldReturnDeliveryData() throws Exception {
            when(deliveryDataService.findByCustomerIdAndDeliveryDataId("cust-1", "dd-001")).thenReturn(deliveryData);
            when(deliveryDataMapper.toResponse(deliveryData)).thenReturn(response);

            mockMvc.perform(get(BASE_URL + "/dd-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("dd-001"))
                    .andExpect(jsonPath("$.direccionEnvio").value("Av. Reforma 123, Col. Centro, CDMX"));
        }

        @Test
        @DisplayName("should return 404 when delivery data not found")
        void shouldReturn404WhenDeliveryDataNotFound() throws Exception {
            when(deliveryDataService.findByCustomerIdAndDeliveryDataId("cust-1", "dd-999"))
                    .thenThrow(new DeliveryDataNotFoundException("cust-1", "dd-999"));

            mockMvc.perform(get(BASE_URL + "/dd-999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404));
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/customers/{customerId}/delivery-data/{deliveryDataId}")
    class UpdateDeliveryData {

        @Test
        @DisplayName("should update delivery data and return 200")
        void shouldUpdateDeliveryData() throws Exception {
            when(deliveryDataMapper.toDomain(any(DeliveryDataRequest.class))).thenReturn(deliveryData);
            when(deliveryDataService.updateDeliveryData(eq("cust-1"), eq("dd-001"), any(DeliveryData.class)))
                    .thenReturn(deliveryData);
            when(deliveryDataMapper.toResponse(deliveryData)).thenReturn(response);

            mockMvc.perform(put(BASE_URL + "/dd-001")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value("dd-001"));
        }

        @Test
        @DisplayName("should return 404 when delivery data not found")
        void shouldReturn404() throws Exception {
            when(deliveryDataMapper.toDomain(any(DeliveryDataRequest.class))).thenReturn(deliveryData);
            when(deliveryDataService.updateDeliveryData(eq("cust-1"), eq("dd-999"), any(DeliveryData.class)))
                    .thenThrow(new DeliveryDataNotFoundException("cust-1", "dd-999"));

            mockMvc.perform(put(BASE_URL + "/dd-999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("should return 400 when validation fails")
        void shouldReturn400WhenInvalid() throws Exception {
            DeliveryDataRequest invalid = new DeliveryDataRequest("");

            mockMvc.perform(put(BASE_URL + "/dd-001")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalid)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/customers/{customerId}/delivery-data/{deliveryDataId}")
    class DeleteDeliveryData {

        @Test
        @DisplayName("should delete delivery data and return 204")
        void shouldDeleteDeliveryData() throws Exception {
            doNothing().when(deliveryDataService).deleteDeliveryData("cust-1", "dd-001");

            mockMvc.perform(delete(BASE_URL + "/dd-001"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("should return 404 when delivery data not found")
        void shouldReturn404() throws Exception {
            doThrow(new DeliveryDataNotFoundException("cust-1", "dd-999"))
                    .when(deliveryDataService).deleteDeliveryData("cust-1", "dd-999");

            mockMvc.perform(delete(BASE_URL + "/dd-999"))
                    .andExpect(status().isNotFound());
        }
    }
}
