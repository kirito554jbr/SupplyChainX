package org.example.supplychainx.Service.Approvisionnement;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Approvisionnement.SupplierDTO;
import org.example.supplychainx.Mappers.Approvisionnement.SupplierMapper;
import org.example.supplychainx.Model.Approvisionnement.RawMaterial;
import org.example.supplychainx.Model.Approvisionnement.Supplier;
import org.example.supplychainx.Repository.Approvisionnement.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class SupplierService {

//    @Autowired
    private SupplierRepository supplierRepository;
    private SupplierMapper supplierMapper;

    public SupplierDTO findById(Long idSupplier){
//        return (SupplierDTO) supplierRepository.findById(idSupplier).stream()
//                .map(supplierMapper::fromEntityToDto);
        Supplier savedSupplier = supplierRepository.findById(idSupplier).get();
        SupplierDTO supplier = supplierMapper.toDto(savedSupplier);
        return supplier;

    }

    public Supplier findByName(String name){
        Supplier savedSupplier = supplierRepository.findByName(name);
//        SupplierDTO supplier = supplierMapper.toDto(savedSupplier);
        return savedSupplier;
    }

    public SupplierDTO nameToDTO(String name){
        Supplier savedSupplier = findByName(name);
        SupplierDTO supplier = supplierMapper.toDto(savedSupplier);
        return supplier;
    }

    public List<SupplierDTO> findAll(){
//      return  supplierRepository.findAll().stream()
//                .map(supplierMapper::fromEntityToDto)
//                .collect(Collectors.toList());
        List<Supplier> suppliers = supplierRepository.findAll();
        List<SupplierDTO> supplierDTOS = suppliers.stream()
                .map(supplier -> supplierMapper.toDto(supplier))
                .collect(Collectors.toList());
        return supplierDTOS;
    }

    public SupplierDTO save(SupplierDTO supplier){
        Supplier savedSupplier = supplierMapper.toEntity(supplier);
        Supplier result = supplierRepository.save(savedSupplier);
        return supplierMapper.toDto(result);
    }

    public void deleteById(Long id){
        supplierRepository.deleteById(id);
    }

    public SupplierDTO update(Long id, SupplierDTO supplier){

        Supplier existingSupplier = supplierRepository.findById(id).orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
        existingSupplier.setName(supplier.getName());
        existingSupplier.setContact(supplier.getContact());
        existingSupplier.setRating(supplier.getRating());
        existingSupplier.setLeadTime(supplier.getLeadTime());
        Supplier updatedSupplier = supplierRepository.save(existingSupplier);
        return supplierMapper.toDto(updatedSupplier);
    }


}
