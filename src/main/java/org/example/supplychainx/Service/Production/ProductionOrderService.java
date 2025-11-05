package org.example.supplychainx.Service.Production;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Production.ProductionOrderDTO;
import org.example.supplychainx.DTO.Production.ProductionOrderResponseDTO;
import org.example.supplychainx.Mappers.Production.ProductionOrderMapper;
import org.example.supplychainx.Model.Production.Product;
import org.example.supplychainx.Model.Production.ProductionOrder;
import org.example.supplychainx.Repository.Production.ProductRepository;
import org.example.supplychainx.Repository.Production.ProductionOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ProductionOrderService {
    private ProductionOrderRepository productionOrderRepository;
    private ProductionOrderMapper productionOrderMapper;
    private ProductRepository productRepository;

    public List<ProductionOrderResponseDTO> findAllProductionOrders() {
        List<ProductionOrder> productionOrders = productionOrderRepository.findAll();
        return productionOrders.stream()
                .map(productionOrderMapper::toResponseDto)
                .toList();
    }

    public ProductionOrderResponseDTO findProductionOrderById(Long id) {
        ProductionOrder productionOrder = productionOrderRepository.findById(id).orElse(null);
        return productionOrderMapper.toResponseDto(productionOrder);
    }

    public ProductionOrderResponseDTO createProductionOrder(ProductionOrderDTO productionOrderDTO) {
        ProductionOrder productionOrder = productionOrderMapper.toEntity(productionOrderDTO);
        Product product = productRepository.findById(productionOrderDTO.getProduct_id()).orElse(null);
        productionOrder.setProduct(product);
        ProductionOrder savedOrder = productionOrderRepository.save(productionOrder);

        return productionOrderMapper.toResponseDto(savedOrder);
    }

    public ProductionOrderResponseDTO updateProductionOrder(Long id, ProductionOrderDTO productionOrderDTO) {
        ProductionOrder productionOrder = productionOrderRepository.findById(id).orElse(null);
        Product product = productRepository.findById(productionOrderDTO.getProduct_id()).orElse(null);
        if (productionOrder != null) {
            productionOrder = productionOrderMapper.toEntity(productionOrderDTO);
            productionOrder.setIdProductionOrder(id);
            productionOrder.setProduct(product);
            ProductionOrder updatedOrder = productionOrderRepository.save(productionOrder);
            return productionOrderMapper.toResponseDto(updatedOrder);
        }
        return null;
    }

    public void deleteProductionOrder(Long id) {
        productionOrderRepository.deleteById(id);
    }
}
