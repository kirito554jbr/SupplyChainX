package org.example.supplychainx.Mappers.Livraison;

import org.example.supplychainx.DTO.Livraison.ProductDTO;
import org.example.supplychainx.DTO.Livraison.ProductResponseDTO;
import org.example.supplychainx.Mappers.Approvisionnement.RawMaterialMapper;
import org.example.supplychainx.Model.Livraison.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RawMaterialMapper.class, BOMMapper.class, ProductionOrderMapper.class})
public interface ProductMapper {

    @Mapping(target = "boms", ignore = true)
    Product toEntity(ProductDTO productDTO);
    ProductDTO toDto(Product product);
    ProductResponseDTO toResponseDTO(Product product);
}
