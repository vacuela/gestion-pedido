package com.retail.management.order.infrastructure.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retail.management.order.application.dto.CustomerRequest;
import com.retail.management.order.application.dto.CustomerResponse;
import com.retail.management.order.application.mapper.CustomerMapper;
import com.retail.management.order.domain.exception.CustomerNotFoundException;
import com.retail.management.order.domain.exception.DuplicateEmailException;
import com.retail.management.order.domain.model.Customer;
import com.retail.management.order.domain.port.in.CustomerServicePort;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CustomerServicePort customerService;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerController customerController;

    private Customer customer;
    private CustomerResponse response;
    private CustomerRequest request;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        customer = new Customer("1", "Juan", "Pérez", "López", "juan@example.com");
        response = new CustomerResponse("1", "Juan", "Pérez", "López", "juan@example.com");
        request = new CustomerRequest("1","Juan", "Pérez", "López", "juan@example.com");
    }

    @Nested
    @DisplayName("POST /api/v1/customers")
    class CreateCustomer {

        @Test
        @DisplayName("should create customer and return 201")
        void shouldCreateCustomer() throws Exception {
            when(customerMapper.toDomain(any(CustomerRequest.class))).thenReturn(customer);
            when(customerService.create(any(Customer.class))).thenReturn(customer);
            when(customerMapper.toResponse(customer)).thenReturn(response);

            mockMvc.perform(post("/api/v1/customers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value("1"))
                    .andExpect(jsonPath("$.firstName").value("Juan"))
                    .andExpect(jsonPath("$.email").value("juan@example.com"));
        }

        @Test
        @DisplayName("should return 400 when validation fails")
        void shouldReturn400WhenInvalid() throws Exception {
            CustomerRequest invalid = new CustomerRequest("","", "", null, "invalid-email");

            mockMvc.perform(post("/api/v1/customers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalid)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.errors").isArray());
        }

        @Test
        @DisplayName("should return 409 when email already exists")
        void shouldReturn409WhenDuplicateEmail() throws Exception {
            when(customerMapper.toDomain(any(CustomerRequest.class))).thenReturn(customer);
            when(customerService.create(any(Customer.class)))
                    .thenThrow(new DuplicateEmailException("juan@example.com"));

            mockMvc.perform(post("/api/v1/customers")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value(409));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/customers/{id}")
    class GetCustomerById {

        @Test
        @DisplayName("should return customer when found")
        void shouldReturnCustomer() throws Exception {
            when(customerService.findById("1")).thenReturn(Optional.of(customer));
            when(customerMapper.toResponse(customer)).thenReturn(response);

            mockMvc.perform(get("/api/v1/customers/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value("1"))
                    .andExpect(jsonPath("$.firstName").value("Juan"));
        }

        @Test
        @DisplayName("should return 404 when not found")
        void shouldReturn404() throws Exception {
            when(customerService.findById("999")).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/customers/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/customers")
    class GetAllCustomers {

        @Test
        @DisplayName("should return all customers")
        void shouldReturnAll() throws Exception {
            CustomerResponse response2 = new CustomerResponse("2", "María", "García", "Ruiz", "maria@example.com");
            Customer customer2 = new Customer("2", "María", "García", "Ruiz", "maria@example.com");

            when(customerService.findAll()).thenReturn(List.of(customer, customer2));
            when(customerMapper.toResponse(customer)).thenReturn(response);
            when(customerMapper.toResponse(customer2)).thenReturn(response2);

            mockMvc.perform(get("/api/v1/customers"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2));
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/customers/{id}")
    class UpdateCustomer {

        @Test
        @DisplayName("should update customer and return 200")
        void shouldUpdateCustomer() throws Exception {
            when(customerMapper.toDomain(any(CustomerRequest.class))).thenReturn(customer);
            when(customerService.update(eq("1"), any(Customer.class))).thenReturn(customer);
            when(customerMapper.toResponse(customer)).thenReturn(response);

            mockMvc.perform(put("/api/v1/customers/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value("1"));
        }

        @Test
        @DisplayName("should return 404 when customer not found")
        void shouldReturn404() throws Exception {
            when(customerMapper.toDomain(any(CustomerRequest.class))).thenReturn(customer);
            when(customerService.update(eq("999"), any(Customer.class)))
                    .thenThrow(new CustomerNotFoundException("999"));

            mockMvc.perform(put("/api/v1/customers/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/customers/{id}")
    class DeleteCustomer {

        @Test
        @DisplayName("should delete customer and return 204")
        void shouldDeleteCustomer() throws Exception {
            doNothing().when(customerService).delete("1");

            mockMvc.perform(delete("/api/v1/customers/1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("should return 404 when customer not found")
        void shouldReturn404() throws Exception {
            doThrow(new CustomerNotFoundException("999")).when(customerService).delete("999");

            mockMvc.perform(delete("/api/v1/customers/999"))
                    .andExpect(status().isNotFound());
        }
    }
}
