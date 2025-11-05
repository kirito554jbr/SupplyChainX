package org.example.supplychainx.Mappers.Livraison;

import org.example.supplychainx.DTO.Livraison.DeliveryDTO;
import org.example.supplychainx.Model.Livraison.Delivery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {OrderMapper.class})
public interface DeliveryMapper {
    Delivery toEntity(DeliveryDTO dto);
    DeliveryDTO toDto(Delivery entity);
}
