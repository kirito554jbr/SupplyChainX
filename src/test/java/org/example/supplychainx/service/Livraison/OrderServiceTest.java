package org.example.supplychainx.service.Livraison;

import org.example.supplychainx.DTO.Livraison.OrderDTO;
import org.example.supplychainx.Mappers.Livraison.OrderMapper;
import org.example.supplychainx.Model.Livraison.Customer;
import org.example.supplychainx.Model.Livraison.Order;
import org.example.supplychainx.Model.Livraison.StatusOrder;
import org.example.supplychainx.Model.Production.Product;
import org.example.supplychainx.Repository.Livraison.CustomerRepository;
import org.example.supplychainx.Repository.Livraison.OrderRepository;
import org.example.supplychainx.Repository.Production.ProductRepository;
import org.example.supplychainx.Service.Livraison.OrderService;
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
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private OrderDTO orderDTO;
    private Product product;
    private Customer customer;

    @BeforeEach
    void setUp() {
        // Setup Product
        product = new Product();
        product.setIdProduct(1L);
        product.setName("Test Product");
        product.setStock(1000);
        product.setCost(50.0);

        // Setup Customer
        customer = new Customer();
        customer.setIdCustomer(1L);
        customer.setName("John Doe");
        customer.setAdress("123 Main Street");
        customer.setCity("Casablanca");

        // Setup Order entity
        order = new Order();
        order.setIdOrder(1L);
        order.setCustomer(customer);
        order.setProduct(product);
        order.setQuantity(100);
        order.setStatus(StatusOrder.EN_PREPARATION);
        order.setDeliveries(new ArrayList<>());

        // Setup OrderDTO
        orderDTO = new OrderDTO();
        orderDTO.setCustomer_id(1L);
        orderDTO.setProduct_id(1L);
        orderDTO.setQuantity(100);
        orderDTO.setStatus(StatusOrder.EN_PREPARATION);
    }

    @Test
    void testFindById_Success() {

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDTO);


        OrderDTO result = orderService.findById(1L);


        assertNotNull(result);
        assertEquals(1L, result.getCustomer_id());
        assertEquals(1L, result.getProduct_id());
        assertEquals(100, result.getQuantity());
        assertEquals(StatusOrder.EN_PREPARATION, result.getStatus());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderMapper, times(1)).toDto(order);
    }

    @Test
    void testFindById_NotFound() {

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());
        when(orderMapper.toDto(null)).thenReturn(null);


        OrderDTO result = orderService.findById(999L);


        assertNull(result);
        verify(orderRepository, times(1)).findById(999L);
    }

    @Test
    void testFindAll_Success() {

        Order order2 = new Order();
        order2.setIdOrder(2L);
        order2.setQuantity(200);
        order2.setStatus(StatusOrder.EN_ROUTE);

        OrderDTO orderDTO2 = new OrderDTO();
        orderDTO2.setCustomer_id(2L);
        orderDTO2.setProduct_id(2L);
        orderDTO2.setQuantity(200);
        orderDTO2.setStatus(StatusOrder.EN_ROUTE);

        List<Order> orders = Arrays.asList(order, order2);
        when(orderRepository.findAll()).thenReturn(orders);
        when(orderMapper.toDto(order)).thenReturn(orderDTO);
        when(orderMapper.toDto(order2)).thenReturn(orderDTO2);


        List<OrderDTO> result = orderService.findAll();


        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(100, result.get(0).getQuantity());
        assertEquals(200, result.get(1).getQuantity());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_EmptyList() {

        when(orderRepository.findAll()).thenReturn(new ArrayList<>());


        List<OrderDTO> result = orderService.findAll();


        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testSave_Success() {

        when(orderMapper.toEntity(orderDTO)).thenReturn(order);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderDTO);


        OrderDTO result = orderService.save(orderDTO);


        assertNotNull(result);
        assertEquals(1L, result.getCustomer_id());
        assertEquals(1L, result.getProduct_id());
        assertEquals(100, result.getQuantity());
        verify(orderMapper, times(1)).toEntity(orderDTO);
        verify(productRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testSave_ProductNotFound() {

        when(orderMapper.toEntity(orderDTO)).thenReturn(order);
        when(productRepository.findById(1L)).thenReturn(Optional.empty());


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.save(orderDTO);
        });

        assertEquals("Product not found with id: 1", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
        verify(customerRepository, never()).findById(any());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testSave_CustomerNotFound() {

        when(orderMapper.toEntity(orderDTO)).thenReturn(order);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.save(orderDTO);
        });

        assertEquals("Customer not found with id: 1", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).findById(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testUpdate_Success() {

        OrderDTO updatedDTO = new OrderDTO();
        updatedDTO.setCustomer_id(1L);
        updatedDTO.setProduct_id(1L);
        updatedDTO.setQuantity(200);
        updatedDTO.setStatus(StatusOrder.LIVREE);

        Order updatedOrder = new Order();
        updatedOrder.setIdOrder(1L);
        updatedOrder.setCustomer(customer);
        updatedOrder.setProduct(product);
        updatedOrder.setQuantity(200);
        updatedOrder.setStatus(StatusOrder.LIVREE);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);
        when(orderMapper.toDto(updatedOrder)).thenReturn(updatedDTO);


        OrderDTO result = orderService.update(1L, updatedDTO);


        assertNotNull(result);
        assertEquals(200, result.getQuantity());
        assertEquals(StatusOrder.LIVREE, result.getStatus());
        verify(orderRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testUpdate_OrderNotFound() {

        when(orderRepository.findById(999L)).thenReturn(Optional.empty());


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.update(999L, orderDTO);
        });

        assertEquals("Order not found with id: 999", exception.getMessage());
        verify(orderRepository, times(1)).findById(999L);
        verify(productRepository, never()).findById(any());
        verify(customerRepository, never()).findById(any());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testUpdate_ProductNotFound() {

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.update(1L, orderDTO);
        });

        assertEquals("Product not found with id: 1", exception.getMessage());
        verify(orderRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(customerRepository, never()).findById(any());
    }

    @Test
    void testUpdate_CustomerNotFound() {

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.update(1L, orderDTO);
        });

        assertEquals("Customer not found with id: 1", exception.getMessage());
        verify(orderRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testDelete_Success() {

        doNothing().when(orderRepository).deleteById(1L);


        orderService.delete(1L);


        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    void testSave_WithDifferentStatuses() {
        // Test EN_PREPARATION
        OrderDTO enPreparation = new OrderDTO();
        enPreparation.setCustomer_id(1L);
        enPreparation.setProduct_id(1L);
        enPreparation.setQuantity(50);
        enPreparation.setStatus(StatusOrder.EN_PREPARATION);

        Order orderEnPreparation = new Order();
        orderEnPreparation.setStatus(StatusOrder.EN_PREPARATION);
        orderEnPreparation.setQuantity(50);

        when(orderMapper.toEntity(enPreparation)).thenReturn(orderEnPreparation);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(orderRepository.save(any(Order.class))).thenReturn(orderEnPreparation);
        when(orderMapper.toDto(orderEnPreparation)).thenReturn(enPreparation);

        OrderDTO result = orderService.save(enPreparation);

        assertEquals(StatusOrder.EN_PREPARATION, result.getStatus());
        assertEquals(50, result.getQuantity());
    }

    @Test
    void testSave_WithEnRouteStatus() {
        // Test EN_ROUTE
        OrderDTO enRoute = new OrderDTO();
        enRoute.setCustomer_id(1L);
        enRoute.setProduct_id(1L);
        enRoute.setQuantity(75);
        enRoute.setStatus(StatusOrder.EN_ROUTE);

        Order orderEnRoute = new Order();
        orderEnRoute.setStatus(StatusOrder.EN_ROUTE);
        orderEnRoute.setQuantity(75);

        when(orderMapper.toEntity(enRoute)).thenReturn(orderEnRoute);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(orderRepository.save(any(Order.class))).thenReturn(orderEnRoute);
        when(orderMapper.toDto(orderEnRoute)).thenReturn(enRoute);

        OrderDTO result = orderService.save(enRoute);

        assertEquals(StatusOrder.EN_ROUTE, result.getStatus());
        assertEquals(75, result.getQuantity());
    }

    @Test
    void testSave_WithLivreeStatus() {
        // Test LIVREE
        OrderDTO livree = new OrderDTO();
        livree.setCustomer_id(1L);
        livree.setProduct_id(1L);
        livree.setQuantity(150);
        livree.setStatus(StatusOrder.LIVREE);

        Order orderLivree = new Order();
        orderLivree.setStatus(StatusOrder.LIVREE);
        orderLivree.setQuantity(150);

        when(orderMapper.toEntity(livree)).thenReturn(orderLivree);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(orderRepository.save(any(Order.class))).thenReturn(orderLivree);
        when(orderMapper.toDto(orderLivree)).thenReturn(livree);

        OrderDTO result = orderService.save(livree);

        assertEquals(StatusOrder.LIVREE, result.getStatus());
        assertEquals(150, result.getQuantity());
    }
}
