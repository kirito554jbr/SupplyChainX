package org.example.supplychainx.DTO.Approvisionnement;

import org.example.supplychainx.Model.Approvisionnement.Supplier;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SupplierDTO2 {
    SupplierDTO toDto();
    Supplier toEntity();
}
