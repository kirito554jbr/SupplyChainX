package org.example.supplychainx.Mappers.Production;

import org.example.supplychainx.DTO.Production.ProductionOrderDTO;
import org.example.supplychainx.DTO.Production.ProductionOrderResponseDTO;
import org.example.supplychainx.Model.Production.ProductionOrder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface ProductionOrderMapper {
    ProductionOrder toEntity(ProductionOrderDTO dto);
    ProductionOrderDTO toDto(ProductionOrder entity);
    ProductionOrderResponseDTO toResponseDto(ProductionOrder entity);
}
