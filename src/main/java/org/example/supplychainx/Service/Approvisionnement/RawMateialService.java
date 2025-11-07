package org.example.supplychainx.Service.Approvisionnement;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Approvisionnement.RawMaterialDTO;
import org.example.supplychainx.DTO.Approvisionnement.SupplierDTO;
import org.example.supplychainx.Mappers.Approvisionnement.RawMaterialMapper;
import org.example.supplychainx.Mappers.Approvisionnement.SupplierMapper;
import org.example.supplychainx.Model.Approvisionnement.RawMaterial;
import org.example.supplychainx.Model.Approvisionnement.Supplier;
import org.example.supplychainx.Repository.Approvisionnement.RawMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Transactional
public class RawMateialService {

    //    @Autowired
    private RawMaterialRepository rawMaterialRepository;
    private RawMaterialMapper rawMaterialMapper;
    private SupplierService supplierService;
    private SupplierMapper supplierMapper;


    public RawMaterialDTO findByIdRawMaterial(Long idRawMaterial) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(idRawMaterial).orElse(null);
        RawMaterialDTO rawMaterialDTO = rawMaterialMapper.toDto(rawMaterial);
        return rawMaterialDTO;
    }

    public List<RawMaterialDTO> findAllRawMaterials() {
        List<RawMaterial> rawMaterials = rawMaterialRepository.findAll();
        List<RawMaterialDTO> materilasDTO = rawMaterials.stream()
                .map(rawMaterial -> rawMaterialMapper.toDto(rawMaterial))
                .toList();
        return materilasDTO;
    }

    public RawMaterialDTO saveRawMaterial(RawMaterialDTO rawMaterialDTO) {

        RawMaterial rawMaterial = rawMaterialMapper.toEntity(rawMaterialDTO);

        List<Supplier> suppliers = rawMaterialDTO.getSuppliers().stream()
                .map(supplierName -> {
                    Supplier supplier = supplierService.findByName(supplierName);
                    if (supplier == null) {
                        throw new RuntimeException("Supplier not found: " + supplierName);
                    }
                    return supplier;
                })
                .collect(Collectors.toList());


        rawMaterial.setSuppliers(suppliers);


        RawMaterial savedMaterial = rawMaterialRepository.save(rawMaterial);
        return rawMaterialMapper.toDto(savedMaterial);
    }


    public void deleteRawMaterial(Long idRawMaterial) {
        rawMaterialRepository.deleteById(idRawMaterial);
    }

    public RawMaterialDTO updateRawMaterial(Long id, RawMaterialDTO rawMaterial) {

        RawMaterial existingRawMaterial = rawMaterialRepository.findById(id).orElse(null);

        List<Supplier> suppliers = rawMaterial.getSuppliers().stream()
                .map(supplierName -> {
                    Supplier supplier = supplierService.findByName(supplierName);
                    if (supplier == null) {
                        throw new RuntimeException("Supplier not found: " + supplierName);
                    }
                    return supplier;
                })
                .collect(Collectors.toList());
        existingRawMaterial.setName(rawMaterial.getName());
        existingRawMaterial.setStock(rawMaterial.getStock());
        existingRawMaterial.setMinStock(rawMaterial.getMinStock());
        existingRawMaterial.setUnit(rawMaterial.getUnit());
        existingRawMaterial.setSuppliers(suppliers);
        RawMaterial result = rawMaterialRepository.save(existingRawMaterial);
        return rawMaterialMapper.toDto(result);
    }

    public void deleteOneSupplierFromRawMaterial(Long idRawMaterial, Long idSupplier) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(idRawMaterial).orElse(null);
        if (rawMaterial != null) {
            List<Supplier> updatedSuppliers = rawMaterial.getSuppliers().stream()
                    .filter(supplier -> !supplier.getIdSupplier().equals(idSupplier))
                    .collect(Collectors.toList());
            rawMaterial.setSuppliers(updatedSuppliers);
            rawMaterialRepository.save(rawMaterial);
        }
    }

    public void addSupplierToRawMaterial(Long idRawMaterial, Long idSupplier) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(idRawMaterial).orElse(null);
        SupplierDTO supplier = supplierService.findById(idSupplier);
        if (rawMaterial != null && supplier != null) {
            List<Supplier> suppliers = rawMaterial.getSuppliers();
            if (suppliers == null) {
                suppliers = new ArrayList<>();
            }
            suppliers.add(supplierMapper.toEntity(supplier));
            rawMaterial.setSuppliers(suppliers);
            rawMaterialRepository.save(rawMaterial);
        }

    }
        public RawMaterial findByName(String name){
            RawMaterial rawMaterial = rawMaterialRepository.findByName(name);
            return rawMaterial;
        }

    public RawMaterial findEntityById(Long id) {
        return rawMaterialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RawMaterial not found with id: " + id));
    }

    public List<RawMaterialDTO> getRawMaterialsWithLowStock() {
        List<RawMaterial> rawMaterials = rawMaterialRepository.findAll();
        List<RawMaterialDTO> lowStockMaterials = rawMaterials.stream()
                .filter(rm -> rm.getStock() < rm.getMinStock())
                .map(rm -> rawMaterialMapper.toDto(rm))
                .collect(toList());
        return lowStockMaterials;
    }
}