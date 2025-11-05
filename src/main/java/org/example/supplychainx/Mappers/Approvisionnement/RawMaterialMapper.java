package org.example.supplychainx.Mappers.Approvisionnement;

import org.example.supplychainx.DTO.Approvisionnement.RawMaterialDTO;
import org.example.supplychainx.DTO.Approvisionnement.SupplierDTO;
import org.example.supplychainx.Model.Approvisionnement.RawMaterial;
import org.example.supplychainx.Model.Approvisionnement.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {SupplierMapper.class})
public interface RawMaterialMapper {
//    SupplierMapper INSTANCE = Mappers.getMapper(SupplierMapper.class);

    @Mapping(target = "suppliers", source = "suppliers")
    RawMaterial toEntity(RawMaterialDTO materialDTO);

//    @Mapping(target = "suppliers", source = "suppliers")
//    RawMaterialDTO toDto(RawMaterial material);

    default RawMaterialDTO toDto(RawMaterial material) {
        if (material == null) {
            return null;
        }

        RawMaterialDTO dto = new RawMaterialDTO();
        dto.setIdRawMaterial(material.getIdMaterial());
        dto.setName(material.getName());
        dto.setStock(material.getStock());
        dto.setMinStock(material.getMinStock());
        dto.setUnit(material.getUnit());

        if (material.getSuppliers() != null) {
            List<String> supplierNames = material.getSuppliers().stream()
                    .map(Supplier::getName)
                    .collect(Collectors.toList());
            dto.setSuppliers(supplierNames);
        }

        return dto;
    }

    default List<Supplier> mapSuppliers(List<String> supplierNames) {
        if (supplierNames == null) {
            return null;
        }
        return supplierNames.stream()
                .map(name -> {
                    Supplier supplier = new Supplier();
                    supplier.setName(name);
                    return supplier;
                })
                .collect(Collectors.toList());
    }


}
