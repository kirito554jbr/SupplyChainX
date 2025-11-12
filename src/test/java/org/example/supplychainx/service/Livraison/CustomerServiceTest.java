package org.example.supplychainx.service.Livraison;

import org.example.supplychainx.DTO.Livraison.CustomerDTO;
import org.example.supplychainx.Mappers.Livraison.CustomerMapper;
import org.example.supplychainx.Model.Livraison.Customer;
import org.example.supplychainx.Repository.Livraison.CustomerRepository;
import org.example.supplychainx.Repository.Livraison.OrderRepository;
import org.example.supplychainx.Service.Livraison.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        // Setup Customer entity
        customer = new Customer();
        customer.setIdCustomer(1L);
        customer.setName("John Doe");
        customer.setAdress("123 Main Street");
        customer.setCity("Casablanca");
        customer.setOrders(new ArrayList<>());

        // Setup CustomerDTO
        customerDTO = new CustomerDTO();
        customerDTO.setName("John Doe");
        customerDTO.setAdress("123 Main Street");
        customerDTO.setCity("Casablanca");
        customerDTO.setOrders(new ArrayList<>());
    }

    @Test
    void testFindById_Success() {

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(customerDTO);

        CustomerDTO result = customerService.findById(1L);


        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("123 Main Street", result.getAdress());
        assertEquals("Casablanca", result.getCity());
        verify(customerRepository, times(1)).findById(1L);
        verify(customerMapper, times(1)).toDto(customer);
    }

    @Test
    void testFindById_NotFound() {

        when(customerRepository.findById(999L)).thenReturn(Optional.empty());
        when(customerMapper.toDto(null)).thenReturn(null);

        CustomerDTO result = customerService.findById(999L);

        assertNull(result);
        verify(customerRepository, times(1)).findById(999L);
    }

    @Test
    void testFindAll_Success() {

        Customer customer2 = new Customer();
        customer2.setIdCustomer(2L);
        customer2.setName("Jane Smith");
        customer2.setAdress("456 Oak Avenue");
        customer2.setCity("Rabat");

        CustomerDTO customerDTO2 = new CustomerDTO();
        customerDTO2.setName("Jane Smith");
        customerDTO2.setAdress("456 Oak Avenue");
        customerDTO2.setCity("Rabat");

        List<Customer> customers = Arrays.asList(customer, customer2);
        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.toDto(customer)).thenReturn(customerDTO);
        when(customerMapper.toDto(customer2)).thenReturn(customerDTO2);


        List<CustomerDTO> result = customerService.findAll();


        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Smith", result.get(1).getName());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_EmptyList() {

        when(customerRepository.findAll()).thenReturn(new ArrayList<>());


        List<CustomerDTO> result = customerService.findAll();


        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testSave_Success() {
        when(customerMapper.toEntity(customerDTO)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.toDto(customer)).thenReturn(customerDTO);


        CustomerDTO result = customerService.save(customerDTO);


        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("123 Main Street", result.getAdress());
        verify(customerMapper, times(1)).toEntity(customerDTO);
        verify(customerRepository, times(1)).save(customer);
        verify(customerMapper, times(1)).toDto(customer);
    }

    @Test
    void testUpdate_Success() {
        CustomerDTO updatedDTO = new CustomerDTO();
        updatedDTO.setName("John Updated");
        updatedDTO.setAdress("789 New Street");
        updatedDTO.setCity("Marrakech");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setIdCustomer(1L);
        updatedCustomer.setName("John Updated");
        updatedCustomer.setAdress("789 New Street");
        updatedCustomer.setCity("Marrakech");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.toEntity(updatedDTO)).thenReturn(updatedCustomer);
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);
        when(customerMapper.toDto(updatedCustomer)).thenReturn(updatedDTO);


        CustomerDTO result = customerService.update(1L, updatedDTO);


        assertNotNull(result);
        assertEquals("John Updated", result.getName());
        assertEquals("789 New Street", result.getAdress());
        assertEquals("Marrakech", result.getCity());
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testUpdate_NotFound() {
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());


        CustomerDTO result = customerService.update(999L, customerDTO);


        assertNull(result);
        verify(customerRepository, times(1)).findById(999L);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void testDeleteById_Success() {
        doNothing().when(customerRepository).deleteById(1L);


        customerService.deleteById(1L);


        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByName_Success() {
        when(customerRepository.findByName("John Doe")).thenReturn(customer);
        when(customerMapper.toDto(customer)).thenReturn(customerDTO);


        CustomerDTO result = customerService.findByName("John Doe");


        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(customerRepository, times(1)).findByName("John Doe");
        verify(customerMapper, times(1)).toDto(customer);
    }

    @Test
    void testFindByName_NotFound() {
        when(customerRepository.findByName("Non Existent")).thenReturn(null);
        when(customerMapper.toDto(null)).thenReturn(null);


        CustomerDTO result = customerService.findByName("Non Existent");


        assertNull(result);
        verify(customerRepository, times(1)).findByName("Non Existent");
    }
}
