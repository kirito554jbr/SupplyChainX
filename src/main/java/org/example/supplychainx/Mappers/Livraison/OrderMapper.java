package org.example.supplychainx.Mappers.Livraison;

import org.example.supplychainx.DTO.Livraison.OrderDTO;
import org.example.supplychainx.Model.Livraison.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, DeliveryMapper.class})
public interface OrderMapper {
    Order toEntity(OrderDTO dto);
    OrderDTO toDto(Order entity);
}
