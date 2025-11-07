package org.example.supplychainx.Controller.Production;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Production.ProductDTO;
import org.example.supplychainx.DTO.Production.ProductResponseDTO;
import org.example.supplychainx.Service.Production.ProductService;
import org.example.supplychainx.annotation.RequiresRole;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
@Tag(name = "Product Management", description = "APIs for managing products and their bill of materials (BOM)")
public class ProductController {
    private ProductService productService;

    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(
            @Parameter(description = "ID of the product to retrieve", required = true)
            @PathVariable long id) {
        try{
            ProductDTO productDTO = productService.getById(id);
            return ResponseEntity.ok(productDTO);
        }catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get all products", description = "Retrieve a list of all products")
    @ApiResponse(responseCode = "200", description = "List of products retrieved successfully")
    @GetMapping
    @RequiresRole({"ADMIN","SUPERVISEUR_PRODUCTION"})
    public ResponseEntity<List<ProductDTO>> getProducts() {
        List<ProductDTO> productDTOS = productService.getAll();
        return ResponseEntity.ok(productDTOS);
    }

    @Operation(summary = "Create new product", description = "Create a new product with its bill of materials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    @RequiresRole({"ADMIN","CHEF_PRODUCTION"})
    public ResponseEntity<ProductResponseDTO> createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Product details with BOM to create", required = true,
                    content = @Content(schema = @Schema(implementation = ProductDTO.class)))
            @RequestBody ProductDTO productDTO) {
        ProductResponseDTO created = productService.save(productDTO);
        return ResponseEntity.status(201).body(created);
    }


    @Operation(summary = "Update product", description = "Update an existing product's information and BOM")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @PutMapping("/{id}")
    @RequiresRole({"ADMIN","CHEF_PRODUCTION"})
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @Parameter(description = "ID of the product to update", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated product details with BOM", required = true,
                    content = @Content(schema = @Schema(implementation = ProductDTO.class)))
            @RequestBody ProductDTO productDTO) {
        ProductResponseDTO updated = productService.updateById(id, productDTO);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete product", description = "Delete a product from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    @RequiresRole({"ADMIN","CHEF_PRODUCTION"})
    public ResponseEntity<String> deleteProduct(
            @Parameter(description = "ID of the product to delete", required = true)
            @PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/search")
    @RequiresRole({"ADMIN","SUPERVISEUR_PRODUCTION"})
    public  ResponseEntity<List<ProductResponseDTO>> searchProductsByName(
            @RequestParam("name") String name) {
        List<ProductResponseDTO> products = productService.findByNameContaining(name);
        return ResponseEntity.ok(products);
    }

}
