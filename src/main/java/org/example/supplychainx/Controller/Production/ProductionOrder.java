package org.example.supplychainx.Controller.Production;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Production.ProductionOrderDTO;
import org.example.supplychainx.DTO.Production.ProductionOrderResponseDTO;
import org.example.supplychainx.Service.Production.ProductionOrderService;
import org.example.supplychainx.annotation.RequiresRole;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/productionOrders")
@Tag(name = "Production Order Management", description = "APIs for managing production orders")
public class ProductionOrder {
    private ProductionOrderService productionOrderService;

    @Operation(summary = "Get all production orders", description = "Retrieve a list of all production orders")
    @ApiResponse(responseCode = "200", description = "List of production orders retrieved successfully")
    @GetMapping
    @RequiresRole({"ADMIN","SUPERVISEUR_PRODUCTION"})
    public ResponseEntity<List<ProductionOrderResponseDTO>> findAllProductionOrders() {
        List<ProductionOrderResponseDTO> productionOrders = productionOrderService.findAllProductionOrders();
        return ResponseEntity.ok(productionOrders);
    }

    @Operation(summary = "Get production order by ID", description = "Retrieve a specific production order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Production order found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductionOrderResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Production order not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductionOrderResponseDTO> findProductionOrderById(
            @Parameter(description = "ID of the production order to retrieve", required = true)
            @PathVariable  Long id) {
        ProductionOrderResponseDTO productionOrderDTO = productionOrderService.findProductionOrderById(id);
        return ResponseEntity.ok(productionOrderDTO);
    }

    @Operation(summary = "Create new production order", description = "Create a new production order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Production order created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductionOrderResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    @RequiresRole({"ADMIN","CHEF_PRODUCTION"})
    public ResponseEntity<ProductionOrderResponseDTO> createProductionOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Production order details to create", required = true,
                    content = @Content(schema = @Schema(implementation = ProductionOrderDTO.class)))
            @RequestBody ProductionOrderDTO productionOrderDTO) {
        ProductionOrderResponseDTO createdOrder = productionOrderService.createProductionOrder(productionOrderDTO);
        return ResponseEntity.status(201).body(createdOrder);
    }

    @Operation(summary = "Update production order", description = "Update an existing production order's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Production order updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductionOrderResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Production order not found", content = @Content)
    })
    @PutMapping("/{id}")
    @RequiresRole({"ADMIN","CHEF_PRODUCTION"})
    public ResponseEntity<ProductionOrderResponseDTO> updateProductionOrder(
            @Parameter(description = "ID of the production order to update", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated production order details", required = true,
                    content = @Content(schema = @Schema(implementation = ProductionOrderDTO.class)))
            @RequestBody ProductionOrderDTO productionOrderDTO) {
        ProductionOrderResponseDTO updatedOrder = productionOrderService.updateProductionOrder(id, productionOrderDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    @Operation(summary = "Delete production order", description = "Delete a production order from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Production order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Production order not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    @RequiresRole({"ADMIN","CHEF_PRODUCTION"})
    public ResponseEntity<String> deleteProductionOrder(
            @Parameter(description = "ID of the production order to delete", required = true)
            @PathVariable Long id) {
        productionOrderService.deleteProductionOrder(id);
        return ResponseEntity.ok("success");
    }
}
