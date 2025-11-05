package org.example.supplychainx.Service.Livraison;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Livraison.OrderDTO;
import org.example.supplychainx.Mappers.Livraison.OrderMapper;
import org.example.supplychainx.Model.Livraison.Order;
import org.example.supplychainx.Repository.Livraison.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

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
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    public OrderDTO update(Long id, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(id).orElse(null);
        if (existingOrder != null) {
            Order orderToUpdate = orderMapper.toEntity(orderDTO);
            orderToUpdate.setIdOrder(id);
            Order updatedOrder = orderRepository.save(orderToUpdate);
            return orderMapper.toDto(updatedOrder);
        }
        return null;
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}
