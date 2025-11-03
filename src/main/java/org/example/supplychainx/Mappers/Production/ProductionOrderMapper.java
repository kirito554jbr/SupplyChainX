package org.example.supplychainx.Mappers.Production;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface ProductionOrderMapper {
//    ProductionOrder toEntity(ProductionOrder dto);
//    ProductionOrder toDto(ProductionOrder entity);
}
