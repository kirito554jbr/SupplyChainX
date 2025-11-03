package org.example.supplychainx.Service.Production;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Production.BOMDTO;
import org.example.supplychainx.Mappers.Production.BOMMapper;
import org.example.supplychainx.Model.Livraison.BOM;
import org.example.supplychainx.Repository.Production.BOMRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BOMService {
    private final BOMRepository BOMRepository;
    private final BOMMapper bomMapper;

    public List<BOMDTO> getAllBOMs() {
        List<BOM> allBOMs = BOMRepository.findAll();
        return  allBOMs.stream()
                .map(bomMapper::toDto)
                .toList();
    }

    public BOMDTO getBOMById(Long id) {
        BOM bom = BOMRepository.findById(id).orElse(null);
        return bomMapper.toDto(bom);
    }

    public List<BOMDTO> getBOMsByProductId(Long productId) {
        List<BOM> boms = BOMRepository.findAll().stream()
                .filter(bom -> bom.getProduct() != null && bom.getProduct().getIdProduct().equals(productId))
                .toList();
        return boms.stream()
                .map(bomMapper::toDto)
                .toList();
    }



    public BOMDTO createBOM(BOMDTO bomDTO) {
        BOM bom = bomMapper.toEntity(bomDTO);
        BOM savedBOM = BOMRepository.save(bom);
        return bomMapper.toDto(savedBOM);
    }

    public BOM save(BOM bom) {
        return BOMRepository.save(bom);
    }


    public BOMDTO updateBOM(Long id, BOMDTO bomDTO) {
        BOM existingBOM = BOMRepository.findById(id).orElse(null);
        if (existingBOM == null) {
            return null;
        }
        BOM bomToUpdate = bomMapper.toEntity(bomDTO);
        bomToUpdate.setIdBOM(id);
        BOM updatedBOM = BOMRepository.save(bomToUpdate);
        return bomMapper.toDto(updatedBOM);
    }

    public void deleteBOM(Long id) {
        BOMRepository.deleteById(id);
    }

    public void deleteByProductId(Long productId) {
        List<BOM> boms = BOMRepository.findAll().stream()
                .filter(bom -> bom.getProduct() != null && bom.getProduct().getIdProduct().equals(productId))
                .toList();
        BOMRepository.deleteAll(boms);
    }
}
