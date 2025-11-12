package org.example.supplychainx.Service.Livraison;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Livraison.DeliveryDTO;
import org.example.supplychainx.Mappers.Livraison.DeliveryMapper;
import org.example.supplychainx.Model.Livraison.Delivery;
import org.example.supplychainx.Model.Livraison.Order;
import org.example.supplychainx.Repository.Livraison.DeliveryRepository;
import org.example.supplychainx.Repository.Livraison.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class DeliveryService {
    private DeliveryRepository deliveryRepository;
    private DeliveryMapper deliveryMapper;
    private OrderRepository orderRepository;

    public List<DeliveryDTO> findAll()
    {
        List<Delivery> deliveries = deliveryRepository.findAll();
        return deliveries.stream()
            .map(deliveryMapper::toDto)
            .toList();
    }

    public  DeliveryDTO findById(Long id) {
        Delivery delivery = deliveryRepository.findById(id).orElse(null);
        return deliveryMapper.toDto(delivery);
    }

    public DeliveryDTO save(DeliveryDTO deliveryDTO) {
        Delivery delivery = deliveryMapper.toEntity(deliveryDTO);
        Order order = orderRepository.findById(deliveryDTO.getOrder_id()).orElse(null);
        delivery.setOrder(order);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return deliveryMapper.toDto(savedDelivery);
    }

    public DeliveryDTO update(Long id, DeliveryDTO deliveryDTO) {
        Delivery existingDelivery = deliveryRepository.findById(id).orElse(null);
        if (existingDelivery != null) {
            Delivery deliveryToUpdate = deliveryMapper.toEntity(deliveryDTO);
            deliveryToUpdate.setIdDelivery(id);
            Delivery updatedDelivery = deliveryRepository.save(deliveryToUpdate);
            return deliveryMapper.toDto(updatedDelivery);
        }
        return null;
    }

    public void delete(Long id) {
        deliveryRepository.deleteById(id);
    }
}
