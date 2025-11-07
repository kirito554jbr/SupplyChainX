package org.example.supplychainx.Service.Production;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Production.BOMDTO;
import org.example.supplychainx.DTO.Production.ProductDTO;
import org.example.supplychainx.DTO.Production.ProductResponseDTO;
import org.example.supplychainx.Mappers.Approvisionnement.RawMaterialMapper;
import org.example.supplychainx.Mappers.Production.BOMMapper;
import org.example.supplychainx.Mappers.Production.ProductMapper;
import org.example.supplychainx.Model.Approvisionnement.RawMaterial;
import org.example.supplychainx.Model.Production.BOM;
import org.example.supplychainx.Model.Production.Product;
import org.example.supplychainx.Repository.Production.ProductRepository;
import org.example.supplychainx.Service.Approvisionnement.RawMateialService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final BOMService bomService;
    private final BOMMapper bomMapper;
    private final RawMateialService rawMaterialService;
    private final RawMaterialMapper rawMaterialMapper;

    public ProductDTO getById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        return productMapper.toDto(product);
    }

    public List<ProductDTO> getAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toDto)
                .toList();
    }


public ProductResponseDTO save(ProductDTO productDTO) {
    Product product = productRepository.save(productMapper.toEntity(productDTO));

    List<BOMDTO> bomDTOs = productDTO.getBoms();

    List<BOM> boms = bomDTOs.stream()
            .map(bomDTO -> {
                BOM bomM = bomMapper.toEntity(bomDTO);
                bomM.setProduct(product);
                RawMaterial rawMaterial = rawMaterialService.findEntityById(bomDTO.getMaterial_id());
                bomM.setMaterial(rawMaterial);
                bomM.setQuantity(bomDTO.getQuantity());
                return bomM;
            })
            .map(bomService::save)
            .toList();

    product.setBoms(boms);
    return productMapper.toResponseDTO(product);
}


    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public ProductResponseDTO updateById(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        // Update product fields
        existingProduct.setName(productDTO.getName());
        existingProduct.setProductionTime(productDTO.getProductionTime());
        existingProduct.setCost(productDTO.getCost());
        existingProduct.setStock(productDTO.getStock());
        Product updatedProduct = productRepository.save(existingProduct);

        // Delete old BOMs
        bomService.deleteByProductId(id);

        // Create new BOMs if provided
        if (productDTO.getBoms() != null && !productDTO.getBoms().isEmpty()) {
            List<BOMDTO> bomDTOs = productDTO.getBoms();

            List<BOM> boms = bomDTOs.stream()
                    .map(bomDTO -> {
                        BOM bomM = bomMapper.toEntity(bomDTO);
                        bomM.setProduct(updatedProduct);
                        RawMaterial rawMaterial = rawMaterialService.findEntityById(bomDTO.getMaterial_id());
                        bomM.setMaterial(rawMaterial);
                        bomM.setQuantity(bomDTO.getQuantity());
                        return bomM;
                    })
                    .map(bomService::save)
                    .toList();

            updatedProduct.setBoms(boms);
        }

        return productMapper.toResponseDTO(updatedProduct);
    }

    public List<ProductDTO> getByName(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        return products.stream()
                .map(productMapper::toDto)
                .toList();
    }

    public List<ProductResponseDTO> findByNameContaining(String name) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
        return products.stream()
                .map(productMapper::toResponseDTO)
                .toList();
    }
}
