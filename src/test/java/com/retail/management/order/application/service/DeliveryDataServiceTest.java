package com.retail.management.order.application.service;

import com.retail.management.order.domain.exception.CustomerNotFoundException;
import com.retail.management.order.domain.exception.DeliveryDataNotFoundException;
import com.retail.management.order.domain.model.Customer;
import com.retail.management.order.domain.model.DeliveryData;
import com.retail.management.order.domain.port.out.CustomerRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryDataServiceTest {

    @Mock
    private CustomerRepositoryPort customerRepository;

    @InjectMocks
    private DeliveryDataService deliveryDataService;

    private Customer customer;
    private DeliveryData deliveryData;

    @BeforeEach
    void setUp() {
        customer = new Customer("1", "Juan", "Pérez", "López", "juan@example.com");
        customer.setDatosEntrega(new ArrayList<>());
        deliveryData = new DeliveryData(null, "Av. Reforma 123, Col. Centro, CDMX");
    }

    @Nested
    @DisplayName("addDeliveryData")
    class AddDeliveryData {

        @Test
        @DisplayName("should add delivery data to customer")
        void shouldAddDeliveryData() {
            when(customerRepository.findById("1")).thenReturn(Optional.of(customer));
            when(customerRepository.save(any(Customer.class))).thenReturn(customer);

            DeliveryData result = deliveryDataService.addDeliveryData("1", deliveryData);

            assertNotNull(result.getId());
            assertEquals("Av. Reforma 123, Col. Centro, CDMX", result.getDireccionEnvio());
            verify(customerRepository).save(any(Customer.class));
        }

        @Test
        @DisplayName("should throw CustomerNotFoundException when customer not found")
        void shouldThrowWhenCustomerNotFound() {
            when(customerRepository.findById("999")).thenReturn(Optional.empty());

            assertThrows(CustomerNotFoundException.class,
                    () -> deliveryDataService.addDeliveryData("999", deliveryData));
            verify(customerRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("findAllByCustomerId")
    class FindAllByCustomerId {

        @Test
        @DisplayName("should return all delivery data for customer")
        void shouldReturnAll() {
            DeliveryData dd1 = new DeliveryData("dd-001", "Av. Reforma 123");
            DeliveryData dd2 = new DeliveryData("dd-002", "Calle Juárez 456");
            customer.setDatosEntrega(new ArrayList<>(List.of(dd1, dd2)));

            when(customerRepository.findById("1")).thenReturn(Optional.of(customer));

            List<DeliveryData> result = deliveryDataService.findAllByCustomerId("1");

            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("should return empty list when customer has no delivery data")
        void shouldReturnEmptyList() {
            when(customerRepository.findById("1")).thenReturn(Optional.of(customer));

            List<DeliveryData> result = deliveryDataService.findAllByCustomerId("1");

            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("should throw CustomerNotFoundException when customer not found")
        void shouldThrowWhenCustomerNotFound() {
            when(customerRepository.findById("999")).thenReturn(Optional.empty());

            assertThrows(CustomerNotFoundException.class,
                    () -> deliveryDataService.findAllByCustomerId("999"));
        }
    }

    @Nested
    @DisplayName("findByCustomerIdAndDeliveryDataId")
    class FindByCustomerIdAndDeliveryDataId {

        @Test
        @DisplayName("should return delivery data when found")
        void shouldReturnDeliveryData() {
            DeliveryData dd = new DeliveryData("dd-001", "Av. Reforma 123");
            customer.setDatosEntrega(new ArrayList<>(List.of(dd)));

            when(customerRepository.findById("1")).thenReturn(Optional.of(customer));

            DeliveryData result = deliveryDataService.findByCustomerIdAndDeliveryDataId("1", "dd-001");

            assertEquals("dd-001", result.getId());
            assertEquals("Av. Reforma 123", result.getDireccionEnvio());
        }

        @Test
        @DisplayName("should throw DeliveryDataNotFoundException when delivery data not found")
        void shouldThrowWhenDeliveryDataNotFound() {
            when(customerRepository.findById("1")).thenReturn(Optional.of(customer));

            assertThrows(DeliveryDataNotFoundException.class,
                    () -> deliveryDataService.findByCustomerIdAndDeliveryDataId("1", "dd-999"));
        }

        @Test
        @DisplayName("should throw CustomerNotFoundException when customer not found")
        void shouldThrowWhenCustomerNotFound() {
            when(customerRepository.findById("999")).thenReturn(Optional.empty());

            assertThrows(CustomerNotFoundException.class,
                    () -> deliveryDataService.findByCustomerIdAndDeliveryDataId("999", "dd-001"));
        }
    }

    @Nested
    @DisplayName("updateDeliveryData")
    class UpdateDeliveryData {

        @Test
        @DisplayName("should update delivery data successfully")
        void shouldUpdateDeliveryData() {
            DeliveryData existing = new DeliveryData("dd-001", "Av. Reforma 123");
            customer.setDatosEntrega(new ArrayList<>(List.of(existing)));

            DeliveryData updated = new DeliveryData(null, "Calle Juárez 456, Col. Nueva");

            when(customerRepository.findById("1")).thenReturn(Optional.of(customer));
            when(customerRepository.save(any(Customer.class))).thenReturn(customer);

            DeliveryData result = deliveryDataService.updateDeliveryData("1", "dd-001", updated);

            assertEquals("dd-001", result.getId());
            assertEquals("Calle Juárez 456, Col. Nueva", result.getDireccionEnvio());
            verify(customerRepository).save(any(Customer.class));
        }

        @Test
        @DisplayName("should throw DeliveryDataNotFoundException when delivery data not found")
        void shouldThrowWhenDeliveryDataNotFound() {
            when(customerRepository.findById("1")).thenReturn(Optional.of(customer));

            DeliveryData updated = new DeliveryData(null, "Calle Juárez 456");

            assertThrows(DeliveryDataNotFoundException.class,
                    () -> deliveryDataService.updateDeliveryData("1", "dd-999", updated));
            verify(customerRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw CustomerNotFoundException when customer not found")
        void shouldThrowWhenCustomerNotFound() {
            when(customerRepository.findById("999")).thenReturn(Optional.empty());

            DeliveryData updated = new DeliveryData(null, "Calle Juárez 456");

            assertThrows(CustomerNotFoundException.class,
                    () -> deliveryDataService.updateDeliveryData("999", "dd-001", updated));
        }
    }

    @Nested
    @DisplayName("deleteDeliveryData")
    class DeleteDeliveryData {

        @Test
        @DisplayName("should delete delivery data successfully")
        void shouldDeleteDeliveryData() {
            DeliveryData dd = new DeliveryData("dd-001", "Av. Reforma 123");
            customer.setDatosEntrega(new ArrayList<>(List.of(dd)));

            when(customerRepository.findById("1")).thenReturn(Optional.of(customer));
            when(customerRepository.save(any(Customer.class))).thenReturn(customer);

            deliveryDataService.deleteDeliveryData("1", "dd-001");

            assertTrue(customer.getDatosEntrega().isEmpty());
            verify(customerRepository).save(any(Customer.class));
        }

        @Test
        @DisplayName("should throw DeliveryDataNotFoundException when delivery data not found")
        void shouldThrowWhenDeliveryDataNotFound() {
            when(customerRepository.findById("1")).thenReturn(Optional.of(customer));

            assertThrows(DeliveryDataNotFoundException.class,
                    () -> deliveryDataService.deleteDeliveryData("1", "dd-999"));
            verify(customerRepository, never()).save(any());
        }

        @Test
        @DisplayName("should throw CustomerNotFoundException when customer not found")
        void shouldThrowWhenCustomerNotFound() {
            when(customerRepository.findById("999")).thenReturn(Optional.empty());

            assertThrows(CustomerNotFoundException.class,
                    () -> deliveryDataService.deleteDeliveryData("999", "dd-001"));
        }
    }
}
