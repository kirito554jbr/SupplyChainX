package org.example.supplychainx.Mappers.Approvisionnement;

import org.example.supplychainx.DTO.Approvisionnement.SupplyOrderDTO;
import org.example.supplychainx.DTO.Approvisionnement.SupplyOrderResponse;
import org.example.supplychainx.Model.Approvisionnement.RawMaterial;
import org.example.supplychainx.Model.Approvisionnement.SupplyOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {SupplierMapper.class, RawMaterialMapper.class})
public interface SupplyOrderMapper {


    SupplyOrder toEntity(SupplyOrderDTO dto);
    SupplyOrderDTO toDto(SupplyOrder entity);
    SupplyOrderResponse toResponse(SupplyOrder supplyOrder);

}
