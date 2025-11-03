package org.example.supplychainx.Mappers.Livraison;

import org.example.supplychainx.DTO.Livraison.BOMDTO;
import org.example.supplychainx.Mappers.Approvisionnement.RawMaterialMapper;
import org.example.supplychainx.Model.Livraison.BOM;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductMapper.class, RawMaterialMapper.class})
public interface BOMMapper {
    BOM toEntity(BOMDTO dto);
    BOMDTO toDto(BOM entity);
}
