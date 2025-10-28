package org.example.supplychainx.Mappers.Approvisionnement;

import org.example.supplychainx.DTO.Approvisionnement.SupplierDTO;
import org.example.supplychainx.Model.Approvisionnement.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {
//    @Autowired
//    private Supplier supplier;

    public Supplier fromDtoToEntity(SupplierDTO dto){
        Supplier supplier = new Supplier();
        supplier.setName(dto.getName());
        supplier.setContact(dto.getContact());
        supplier.setRating(dto.getRating());
        supplier.setLeadTime(dto.getLeadTime());
        return supplier;
    }

    public SupplierDTO fromEntityToDto(Supplier entity){
        SupplierDTO supplier = new SupplierDTO();
        supplier.setIdSupplier(entity.getIdSupplier());
        supplier.setName(entity.getName());
        supplier.setContact(entity.getContact());
        supplier.setRating(entity.getRating());
        supplier.setLeadTime(entity.getLeadTime());
        return supplier;
    }
}
