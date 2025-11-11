package org.example.supplychainx.Mappers.Approvisionnement;

import org.example.supplychainx.DTO.Approvisionnement.SupplyOrderDTO;
import org.example.supplychainx.DTO.Approvisionnement.SupplyOrderResponse;
import org.example.supplychainx.Model.Approvisionnement.SupplyOrder;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = {SupplierMapper.class, RawMaterialMapper.class})
public interface SupplyOrderMapper {


    SupplyOrder toEntity(SupplyOrderDTO dto);
    SupplyOrderDTO toDto(SupplyOrder entity);
    SupplyOrderResponse toResponse(SupplyOrder supplyOrder);

}
