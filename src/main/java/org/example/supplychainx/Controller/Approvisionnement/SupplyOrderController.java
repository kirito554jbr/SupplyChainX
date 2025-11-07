package org.example.supplychainx.Controller.Approvisionnement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Approvisionnement.SupplyOrderDTO;
import org.example.supplychainx.DTO.Approvisionnement.SupplyOrderRequest;
import org.example.supplychainx.DTO.Approvisionnement.SupplyOrderResponse;
import org.example.supplychainx.Model.Approvisionnement.SupplyOrder;
import org.example.supplychainx.Service.Approvisionnement.SupplyOrderService;
import org.example.supplychainx.annotation.RequiresRole;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supply-orders")
@CrossOrigin(origins = "*")
@AllArgsConstructor
@Tag(name = "Supply Order Management", description = "APIs for managing supply orders from suppliers")
public class SupplyOrderController {

    private final SupplyOrderService supplyOrderService;

    @Operation(summary = "Get all supply orders", description = "Retrieve a list of all supply orders")
    @ApiResponse(responseCode = "200", description = "List of supply orders retrieved successfully")
    @GetMapping
    @RequiresRole({"ADMIN","SUPERVISEUR_LOGISTIQUE"})
    public ResponseEntity<List<SupplyOrderDTO>> getAllSupplyOrders() {
        List<SupplyOrderDTO> supplyOrders = supplyOrderService.findAll();
        return ResponseEntity.ok(supplyOrders);
    }

    @Operation(summary = "Get supply order by ID", description = "Retrieve a specific supply order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supply order found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SupplyOrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Supply order not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<SupplyOrderDTO> getSupplyOrderById(
            @Parameter(description = "ID of the supply order to retrieve", required = true)
            @PathVariable Long id) {
        try {
            SupplyOrderDTO supplyOrder = supplyOrderService.findById(id);
            return ResponseEntity.ok(supplyOrder);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create new supply order", description = "Create a new supply order with raw materials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Supply order created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SupplyOrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })

    @PostMapping
    @RequiresRole({"ADMIN","RESPONSABLE_ACHATS"})
    public ResponseEntity<SupplyOrderResponse> createSupplyOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Supply order details to create", required = true,
                    content = @Content(schema = @Schema(implementation = SupplyOrderRequest.class)))
            @RequestBody SupplyOrderRequest supplyOrder) {
        SupplyOrderResponse created = supplyOrderService.save(supplyOrder);
        return ResponseEntity.status(201).body(created);
    }



    @Operation(summary = "Update supply order", description = "Update an existing supply order's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supply order updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SupplyOrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Supply order not found", content = @Content)
    })

    @PutMapping("/{id}")
    @RequiresRole({"ADMIN","RESPONSABLE_ACHATS"})
    public ResponseEntity<SupplyOrderDTO> updateSupplyOrder(
            @Parameter(description = "ID of the supply order to update", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated supply order details", required = true,
                    content = @Content(schema = @Schema(implementation = SupplyOrderDTO.class)))
            @RequestBody SupplyOrderDTO supplyOrder) {
        SupplyOrderDTO updated = supplyOrderService.update(id, supplyOrder);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete supply order", description = "Delete a supply order from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supply order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Supply order not found", content = @Content)
    })

    @DeleteMapping("/{id}")
    @RequiresRole({"ADMIN","RESPONSABLE_ACHATS"})
    public ResponseEntity<String> deleteSupplyOrder(
            @Parameter(description = "ID of the supply order to delete", required = true)
            @PathVariable Long id) {
        supplyOrderService.delete(id);
        return ResponseEntity.ok("success");
    }
}
