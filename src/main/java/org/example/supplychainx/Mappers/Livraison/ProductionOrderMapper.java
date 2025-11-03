package org.example.supplychainx.Mappers.Livraison;

import org.example.supplychainx.Model.Livraison.ProductionOrder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface ProductionOrderMapper {
//    ProductionOrder toEntity(ProductionOrder dto);
//    ProductionOrder toDto(ProductionOrder entity);
}
