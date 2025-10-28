package org.example.supplychainx.Service.Approvisionnement;

import org.example.supplychainx.DTO.Approvisionnement.SupplierDTO;
import org.example.supplychainx.Mappers.Approvisionnement.SupplierMapper;
import org.example.supplychainx.Model.Approvisionnement.Supplier;
import org.example.supplychainx.Repository.Approvisionnement.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierService {

//    @Autowired
    private SupplierRepository supplierRepository;
    private SupplierMapper supplierMapper;

    public SupplierService(SupplierRepository supplierRepository, SupplierMapper supplierMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierMapper = supplierMapper;
    }

    public SupplierDTO findById(Long idSupplier){
        return (SupplierDTO) supplierRepository.findById(idSupplier).stream()
                .map(supplierMapper::fromEntityToDto);
    }

    public List<SupplierDTO> findAll(){
      return  supplierRepository.findAll().stream()
                .map(supplierMapper::fromEntityToDto)
                .collect(Collectors.toList());
    }

    public SupplierDTO save(Supplier supplier){
        Supplier SavedSupplier = supplierRepository.save(supplier);
        return supplierMapper.fromEntityToDto(SavedSupplier);
    }

    public void deleteById(Long id){
        supplierRepository.deleteById(id);
    }

    public SupplierDTO update(Long id, Supplier supplier){
        SupplierDTO supplier1 = findById(id);
        Supplier supplierEntity = supplierMapper.fromDtoToEntity(supplier1);
        supplierEntity.setName(supplier.getName());
        supplierEntity.setContact(supplier.getContact());
        supplierEntity.setRating(supplier.getRating());
        supplierEntity.setLeadTime(supplier.getLeadTime());
        SupplierDTO result = supplierMapper.fromEntityToDto(supplierRepository.save(supplierEntity));
        return result;
    }


}
