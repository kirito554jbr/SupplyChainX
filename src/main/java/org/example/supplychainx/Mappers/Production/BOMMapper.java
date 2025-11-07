package org.example.supplychainx.Mappers.Production;

import org.example.supplychainx.DTO.Production.BOMDTO;
import org.example.supplychainx.Mappers.Approvisionnement.RawMaterialMapper;
import org.example.supplychainx.Model.Production.BOM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, RawMaterialMapper.class})
public interface BOMMapper {

    BOM toEntity(BOMDTO dto);

    @Mapping(target = "product_id", source = "product.idProduct")
    @Mapping(target = "material_id", source = "material.idMaterial")
    BOMDTO toDto(BOM entity);
}
