package com.retail.management.order.application.service;

import com.retail.management.order.domain.exception.CustomerNotFoundException;
import com.retail.management.order.domain.exception.DuplicateEmailException;
import com.retail.management.order.domain.model.Customer;
import com.retail.management.order.domain.port.out.CustomerRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepositoryPort customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer("1", "Juan", "Pérez", "López", "juan@example.com");
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("should create customer when email does not exist")
        void shouldCreateCustomer() {
            when(customerRepository.existsByEmail("juan@example.com")).thenReturn(false);
            when(customerRepository.save(any(Customer.class))).thenReturn(customer);

            Customer result = customerService.create(customer);

            assertNotNull(result);
            assertEquals("Juan", result.getNombre());
            verify(customerRepository).existsByEmail("juan@example.com");
            verify(customerRepository).save(customer);
        }

        @Test
        @DisplayName("should throw DuplicateEmailException when email already exists")
        void shouldThrowWhenEmailExists() {
            when(customerRepository.existsByEmail("juan@example.com")).thenReturn(true);

            assertThrows(DuplicateEmailException.class, () -> customerService.create(customer));
            verify(customerRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("should return customer when found")
        void shouldReturnCustomer() {
            when(customerRepository.findById("1")).thenReturn(Optional.of(customer));

            Optional<Customer> result = customerService.findById("1");

            assertTrue(result.isPresent());
            assertEquals("Juan", result.get().getNombre());
        }

        @Test
        @DisplayName("should return empty when not found")
        void shouldReturnEmpty() {
            when(customerRepository.findById("999")).thenReturn(Optional.empty());

            Optional<Customer> result = customerService.findById("999");

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {

        @Test
        @DisplayName("should return all customers")
        void shouldReturnAll() {
            Customer customer2 = new Customer("2", "María", "García", "Ruiz", "maria@example.com");
            when(customerRepository.findAll()).thenReturn(List.of(customer, customer2));

            List<Customer> result = customerService.findAll();

            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("should return empty list when no customers")
        void shouldReturnEmptyList() {
            when(customerRepository.findAll()).thenReturn(List.of());

            List<Customer> result = customerService.findAll();

            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("update")
    class Update {

        @Test
        @DisplayName("should update customer when found and email is unique")
        void shouldUpdateCustomer() {
            Customer updated = new Customer(null, "Juan Carlos", "Pérez", "López", "juanc@example.com");
            Customer saved = new Customer("1", "Juan Carlos", "Pérez", "López", "juanc@example.com");

            when(customerRepository.findById("1")).thenReturn(Optional.of(customer));
            when(customerRepository.findByEmail("juanc@example.com")).thenReturn(Optional.empty());
            when(customerRepository.save(any(Customer.class))).thenReturn(saved);

            Customer result = customerService.update("1", updated);

            assertEquals("Juan Carlos", result.getNombre());
            assertEquals("juanc@example.com", result.getEmail());
        }

        @Test
        @DisplayName("should allow update when email belongs to same customer")
        void shouldAllowSameEmail() {
            Customer updated = new Customer(null, "Juan Carlos", "Pérez", "López", "juan@example.com");
            Customer saved = new Customer("1", "Juan Carlos", "Pérez", "López", "juan@example.com");

            when(customerRepository.findById("1")).thenReturn(Optional.of(customer));
            when(customerRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(customer));
            when(customerRepository.save(any(Customer.class))).thenReturn(saved);

            Customer result = customerService.update("1", updated);

            assertEquals("Juan Carlos", result.getNombre());
        }

        @Test
        @DisplayName("should throw CustomerNotFoundException when customer not found")
        void shouldThrowWhenNotFound() {
            Customer updated = new Customer(null, "Juan", "Pérez", "López", "juan@example.com");
            when(customerRepository.findById("999")).thenReturn(Optional.empty());

            assertThrows(CustomerNotFoundException.class, () -> customerService.update("999", updated));
        }

        @Test
        @DisplayName("should throw DuplicateEmailException when email belongs to another customer")
        void shouldThrowWhenEmailBelongsToAnother() {
            Customer another = new Customer("2", "María", "García", "Ruiz", "maria@example.com");
            Customer updated = new Customer(null, "Juan", "Pérez", "López", "maria@example.com");

            when(customerRepository.findById("1")).thenReturn(Optional.of(customer));
            when(customerRepository.findByEmail("maria@example.com")).thenReturn(Optional.of(another));

            assertThrows(DuplicateEmailException.class, () -> customerService.update("1", updated));
            verify(customerRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("should delete customer when exists")
        void shouldDeleteCustomer() {
            when(customerRepository.existsById("1")).thenReturn(true);

            customerService.delete("1");

            verify(customerRepository).deleteById("1");
        }

        @Test
        @DisplayName("should throw CustomerNotFoundException when not exists")
        void shouldThrowWhenNotExists() {
            when(customerRepository.existsById("999")).thenReturn(false);

            assertThrows(CustomerNotFoundException.class, () -> customerService.delete("999"));
            verify(customerRepository, never()).deleteById(any());
        }
    }
}
