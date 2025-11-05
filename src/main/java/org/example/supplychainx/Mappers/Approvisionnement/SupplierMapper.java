package org.example.supplychainx.Mappers.Approvisionnement;

import org.example.supplychainx.DTO.Approvisionnement.SupplierDTO;
import org.example.supplychainx.Model.Approvisionnement.RawMaterial;
import org.example.supplychainx.Model.Approvisionnement.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
//
//    Supplier toEntity(SupplierDTO supplierDTO);
//
//    SupplierDTO toDto(Supplier supplier);


//    @Mapping(target = "rawMaterials", source = "rawMaterials")
    Supplier toEntity(SupplierDTO supplierDTO);

    SupplierDTO toDto(Supplier supplier);
//    {
//        if(supplier==null){
//            return null;
//        }
//        SupplierDTO dto = new SupplierDTO();
//        dto.setIdSupplier(supplier.getIdSupplier());
//        dto.setName(supplier.getName());
//        dto.setContact(supplier.getContact());
//        dto.setRating(supplier.getRating());
//        dto.setLeadTime(supplier.getLeadTime());
//
//        if (supplier.getRawMaterials() != null) {
//            List<String> rawMaterialNames = supplier.getRawMaterials().stream()
//                    .map(RawMaterial::getName)
//                    .collect(Collectors.toList());
//            dto.setRawMaterials(rawMaterialNames);
//        }
//
//        return dto;
//    };
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
