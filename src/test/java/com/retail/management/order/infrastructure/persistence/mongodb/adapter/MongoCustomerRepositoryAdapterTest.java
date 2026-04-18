package com.retail.management.order.infrastructure.persistence.mongodb.adapter;

import com.retail.management.order.domain.model.Customer;
import com.retail.management.order.infrastructure.persistence.mongodb.document.CustomerDocument;
import com.retail.management.order.infrastructure.persistence.mongodb.mapper.CustomerDocumentMapper;
import com.retail.management.order.infrastructure.persistence.mongodb.repository.SpringDataMongoCustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class MongoCustomerRepositoryAdapterTest {

    @Mock
    private SpringDataMongoCustomerRepository mongoRepository;

    @Mock
    private CustomerDocumentMapper mapper;

    @InjectMocks
    private MongoCustomerRepositoryAdapter adapter;

    private Customer customer;
    private CustomerDocument document;

    @BeforeEach
    void setUp() {
        customer = new Customer("1", "Juan", "Pérez", "López", "juan@example.com");
        document = new CustomerDocument("1", "Juan", "Pérez", "López", "juan@example.com");
    }

    @Test
    @DisplayName("should save customer")
    void shouldSave() {
        when(mapper.toDocument(customer)).thenReturn(document);
        when(mongoRepository.save(document)).thenReturn(document);
        when(mapper.toDomain(document)).thenReturn(customer);

        Customer result = adapter.save(customer);

        assertEquals("1", result.getUserId());
        verify(mongoRepository).save(document);
    }

    @Test
    @DisplayName("should find customer by id")
    void shouldFindById() {
        when(mongoRepository.findById("1")).thenReturn(Optional.of(document));
        when(mapper.toDomain(document)).thenReturn(customer);

        Optional<Customer> result = adapter.findById("1");

        assertTrue(result.isPresent());
        assertEquals("Juan", result.get().getNombre());
    }

    @Test
    @DisplayName("should return empty when customer not found by id")
    void shouldReturnEmptyWhenNotFoundById() {
        when(mongoRepository.findById("999")).thenReturn(Optional.empty());

        Optional<Customer> result = adapter.findById("999");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("should find all customers")
    void shouldFindAll() {
        CustomerDocument doc2 = new CustomerDocument("2", "María", "García", "Ruiz", "maria@example.com");
        Customer cust2 = new Customer("2", "María", "García", "Ruiz", "maria@example.com");

        when(mongoRepository.findAll()).thenReturn(List.of(document, doc2));
        when(mapper.toDomain(document)).thenReturn(customer);
        when(mapper.toDomain(doc2)).thenReturn(cust2);

        List<Customer> result = adapter.findAll();

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("should delete customer by id")
    void shouldDeleteById() {
        adapter.deleteById("1");

        verify(mongoRepository).deleteById("1");
    }

    @Test
    @DisplayName("should check if customer exists by id")
    void shouldExistsById() {
        when(mongoRepository.existsById("1")).thenReturn(true);

        assertTrue(adapter.existsById("1"));
    }

    @Test
    @DisplayName("should check if customer exists by email")
    void shouldExistsByEmail() {
        when(mongoRepository.existsByEmail("juan@example.com")).thenReturn(true);

        assertTrue(adapter.existsByEmail("juan@example.com"));
    }

    @Test
    @DisplayName("should find customer by email")
    void shouldFindByEmail() {
        when(mongoRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(document));
        when(mapper.toDomain(document)).thenReturn(customer);

        Optional<Customer> result = adapter.findByEmail("juan@example.com");

        assertTrue(result.isPresent());
        assertEquals("juan@example.com", result.get().getEmail());
    }
}
