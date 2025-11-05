package org.example.supplychainx.Mappers.Livraison;

import org.example.supplychainx.DTO.Livraison.CustomerDTO;
import org.example.supplychainx.Model.Livraison.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {OrderMapper.class})
public interface CustomerMapper {
    Customer toEntity(CustomerDTO dto);
    CustomerDTO toDto(Customer entity);
}
