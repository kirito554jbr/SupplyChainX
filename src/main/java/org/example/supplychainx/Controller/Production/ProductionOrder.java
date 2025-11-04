package org.example.supplychainx.Controller.Production;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Production.ProductionOrderDTO;
import org.example.supplychainx.DTO.Production.ProductionOrderResponseDTO;
import org.example.supplychainx.Service.Production.ProductionOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/productionOrders")
public class ProductionOrder {
    private ProductionOrderService productionOrderService;

    @GetMapping
    public ResponseEntity<List<ProductionOrderResponseDTO>> findAllProductionOrders() {
        List<ProductionOrderResponseDTO> productionOrders = productionOrderService.findAllProductionOrders();
        return ResponseEntity.ok(productionOrders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductionOrderResponseDTO> findProductionOrderById(@PathVariable  Long id) {
        ProductionOrderResponseDTO productionOrderDTO = productionOrderService.findProductionOrderById(id);
        return ResponseEntity.ok(productionOrderDTO);
    }

    @PostMapping
    public ResponseEntity<ProductionOrderResponseDTO> createProductionOrder(@RequestBody ProductionOrderDTO productionOrderDTO) {
        ProductionOrderResponseDTO createdOrder = productionOrderService.createProductionOrder(productionOrderDTO);
        return ResponseEntity.status(201).body(createdOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductionOrderResponseDTO> updateProductionOrder(@PathVariable Long id, @RequestBody ProductionOrderDTO productionOrderDTO) {
        ProductionOrderResponseDTO updatedOrder = productionOrderService.updateProductionOrder(id, productionOrderDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductionOrder(@PathVariable Long id) {
        productionOrderService.deleteProductionOrder(id);
        return ResponseEntity.ok("success");
    }
}
