package org.example.supplychainx.Controller.Production;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Production.ProductDTO;
import org.example.supplychainx.DTO.Production.ProductResponseDTO;
import org.example.supplychainx.Service.Production.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {
    private ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable long id) {
        try{
            ProductDTO productDTO = productService.getById(id);
            return ResponseEntity.ok(productDTO);
        }catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProducts() {
        List<ProductDTO> productDTOS = productService.getAll();
        return ResponseEntity.ok(productDTOS);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductResponseDTO created = productService.save(productDTO);
        return ResponseEntity.status(201).body(created);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        ProductResponseDTO updated = productService.updateById(id, productDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.ok("success");
    }

}
