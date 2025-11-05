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

//    @Mapping(target = "supplyOrderMaterials", source = "supplyOrderMaterials")
    SupplyOrder toEntity(SupplyOrderDTO dto);
    SupplyOrderDTO toDto(SupplyOrder entity);
    SupplyOrderResponse toResponse(SupplyOrder supplyOrder);
//
//    default SupplyOrder toDto(SupplyOrder entity) {
//        if (entity == null) {
//            return null;
//        }
//
//        SupplyOrderDTO dto = new SupplyOrderDTO();
//        dto.setIdOrder(entity.getIdOrder());
//        dto.setOrderDate(entity.getOrderDate());
//        dto.setStatus(entity.getStatus());
//        dto.setSupplierName(entity.getSupplier().getName());
//
//            if (entity.getSupplyOrderMaterials() != null) {
//            List<String> rawMaterialNames = entity.getSupplyOrderMaterials().stream()
//                    .map(RawMaterial::getName)
//                    .collect(Collectors.toList());
//            dto.setSupplyOrderMaterials(rawMaterialNames);
//        }
//
//        return dto;
//    }
//
//
//    default List<RawMaterial> mapRawMaterials(List<String> rawMaterialNames) {
//        if (rawMaterialNames == null) {
//            return null;
//        }
//        return rawMaterialNames.stream()
//                .map(name -> {
//                    RawMaterial rawMaterial = new RawMaterial();
//                    rawMaterial.setName(name);
//                    return rawMaterial;
//                })
//                .collect(Collectors.toList());
//    }

}
