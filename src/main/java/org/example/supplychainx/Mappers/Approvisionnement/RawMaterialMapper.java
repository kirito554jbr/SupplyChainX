package org.example.supplychainx.Mappers.Approvisionnement;

import org.example.supplychainx.DTO.Approvisionnement.RawMaterialDTO;
import org.example.supplychainx.Model.Approvisionnement.RawMaterial;

public class RawMaterialMapper {

    public RawMaterial fromdtoToEntity(RawMaterialDTO dto) {
        RawMaterial material = new RawMaterial();
//        material.setIdMaterial(dto.getIdRawMaterial());
        material.setName(dto.getName());
        material.setStock(dto.getStock());
        material.setMinStock(dto.getMinStock());
        material.setUnit(dto.getUnit());
//        material.setSupplier(dto.getIdSupplier());

        return material;
    }

    public RawMaterialDTO fromEntityToDto(RawMaterial entity) {
        RawMaterialDTO dto = new RawMaterialDTO();
        dto.setIdRawMaterial(entity.getIdMaterial());
        dto.setName(entity.getName());
        dto.setStock(entity.getStock());
        dto.setMinStock(entity.getMinStock());
        dto.setUnit(entity.getUnit());
        dto.setIdSupplier(entity.getSupplier().getIdSupplier());
        dto.setSupplierName(entity.getSupplier().getName());
        return dto;
    }
}
