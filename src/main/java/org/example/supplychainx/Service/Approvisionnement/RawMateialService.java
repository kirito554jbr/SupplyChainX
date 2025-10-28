package org.example.supplychainx.Service.Approvisionnement;

import org.example.supplychainx.Mappers.Approvisionnement.RawMaterialMapper;
import org.example.supplychainx.Model.Approvisionnement.RawMaterial;
import org.example.supplychainx.Model.Approvisionnement.Supplier;
import org.example.supplychainx.Repository.Approvisionnement.RawMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RawMateialService {

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    public RawMaterial findByIdRawMaterial(Long idRawMaterial){
        return rawMaterialRepository.findById(idRawMaterial).orElse(null);
    }

    public List<RawMaterial> findAllRawMaterials(){
        return  rawMaterialRepository.findAll();
    }

    public RawMaterial saveRawMaterial(RawMaterial rawMaterial){
        return rawMaterialRepository.save(rawMaterial);
    }

    public void deleteRawMaterial(RawMaterial rawMaterial){
        rawMaterialRepository.delete(rawMaterial);
    }

    public RawMaterial updateRawMaterial(RawMaterialDTO rawMaterial){
        RawMaterialMapper mapper = new RawMaterialMapper();
        RawMaterial rawMaterial1 = mapper.fromdtoToEntity(rawMaterial);

        Supplier supplier = supplierRepository.findById(rawMaterial.getSupplierId()).orElse(null);
        rawMaterial1.setSupplier(supplier);
        RawMaterial rawMaterial1 = findByIdRawMaterial(rawMaterial.getIdMaterial());
        rawMaterial1.setName(rawMaterial.getName());
        rawMaterial1.setStock(rawMaterial.getStock());
        rawMaterial1.setMinStock(rawMaterial.getMinStock());
        rawMaterial1.setUnit(rawMaterial.getUnit());
        rawMaterial1.setSupplier(rawMaterial.getSupplier());
        return rawMaterialRepository.save(rawMaterial1);
    }

//    public List<RawMaterial> findAllRawMaterialsByIdSupplier(Long idSupplier){
//        return  rawMaterialRepository.findAllBySupplierId(idSupplier);
//    }
}
