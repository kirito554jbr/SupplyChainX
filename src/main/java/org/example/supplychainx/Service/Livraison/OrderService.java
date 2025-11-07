package org.example.supplychainx.Service.Livraison;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Livraison.OrderDTO;
import org.example.supplychainx.Mappers.Livraison.OrderMapper;
import org.example.supplychainx.Model.Livraison.Customer;
import org.example.supplychainx.Model.Livraison.Order;
import org.example.supplychainx.Model.Production.Product;
import org.example.supplychainx.Repository.Livraison.CustomerRepository;
import org.example.supplychainx.Repository.Livraison.OrderRepository;
import org.example.supplychainx.Repository.Production.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public OrderDTO findById(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        return orderMapper.toDto(order);
    }

    public List<OrderDTO> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    public OrderDTO save(OrderDTO orderDTO) {
        Order order = orderMapper.toEntity(orderDTO);

        // Fetch and validate customer and product
        Product product = productRepository.findById(orderDTO.getProduct_id())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + orderDTO.getProduct_id()));
        Customer customer = customerRepository.findById(orderDTO.getCustomer_id())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + orderDTO.getCustomer_id()));

        order.setProduct(product);
        order.setCustomer(customer);

        Order savedOrder = orderRepository.save(order);
        OrderDTO savedOrderDTO = orderMapper.toDto(savedOrder);
        savedOrderDTO.setProduct_id(product.getIdProduct());
        savedOrderDTO.setCustomer_id(customer.getIdCustomer());
        return savedOrderDTO;
    }

    public OrderDTO update(Long id, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // Fetch and set the customer and product relationships
        Product product = productRepository.findById(orderDTO.getProduct_id())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + orderDTO.getProduct_id()));
        Customer customer = customerRepository.findById(orderDTO.getCustomer_id())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + orderDTO.getCustomer_id()));

        existingOrder.setCustomer(customer);
        existingOrder.setProduct(product);
        existingOrder.setQuantity(orderDTO.getQuantity());
        existingOrder.setStatus(orderDTO.getStatus());

        Order updatedOrder = orderRepository.save(existingOrder);
        return orderMapper.toDto(updatedOrder);
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}
