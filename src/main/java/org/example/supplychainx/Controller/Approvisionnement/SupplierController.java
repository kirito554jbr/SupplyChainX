package org.example.supplychainx.Controller.Approvisionnement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Approvisionnement.RawMaterialDTO;
import org.example.supplychainx.DTO.Approvisionnement.SupplierDTO;
import org.example.supplychainx.Service.Approvisionnement.SupplierService;
import org.example.supplychainx.annotation.RequiresRole;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "*")
@AllArgsConstructor
@Tag(name = "Supplier Management", description = "APIs for managing suppliers in the supply chain")
public class SupplierController {

    private final SupplierService supplierService;


    @Operation(summary = "Get all suppliers", description = "Retrieve a list of all suppliers")
    @ApiResponse(responseCode = "200", description = "List of suppliers retrieved successfully")
    @GetMapping
    @RequiresRole({"ADMIN","SUPERVISEUR_LOGISTIQUE"})
    public List<SupplierDTO> getAllSuppliers() {
        return supplierService.findAll();
    }

    @Operation(summary = "Get supplier by ID", description = "Retrieve a specific supplier by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SupplierDTO.class))),
            @ApiResponse(responseCode = "404", description = "Supplier not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> getSupplierById(
            @Parameter(description = "ID of the supplier to retrieve", required = true)
            @PathVariable Long id) {
        try {
            SupplierDTO supplier = supplierService.findById(id);
            return ResponseEntity.ok(supplier);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create new supplier", description = "Create a new supplier in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Supplier created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SupplierDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })

    @PostMapping
    @RequiresRole({"ADMIN","GESTIONNAIRE_APPROVISIONNEMENT"})
    public ResponseEntity<SupplierDTO> createSupplier(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Supplier details to create", required = true,
                    content = @Content(schema = @Schema(implementation = SupplierDTO.class)))
            @RequestBody SupplierDTO supplier) {
        SupplierDTO created = supplierService.save(supplier);
        return ResponseEntity.status(201).body(created);
    }

    @Operation(summary = "Update supplier", description = "Update an existing supplier's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SupplierDTO.class))),
            @ApiResponse(responseCode = "404", description = "Supplier not found", content = @Content)
    })

    @PutMapping("/{id}")
    @RequiresRole({"ADMIN","GESTIONNAIRE_APPROVISIONNEMENT"})
    public ResponseEntity<SupplierDTO> updateSupplier(
            @Parameter(description = "ID of the supplier to update", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated supplier details", required = true,
                    content = @Content(schema = @Schema(implementation = SupplierDTO.class)))
            @RequestBody SupplierDTO supplier) {
        SupplierDTO updated = supplierService.update(id, supplier);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete supplier", description = "Delete a supplier from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Supplier deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found", content = @Content)
    })

    @DeleteMapping("/{id}")
    @RequiresRole({"ADMIN","GESTIONNAIRE_APPROVISIONNEMENT"})
    public ResponseEntity<Void> deleteSupplier(
            @Parameter(description = "ID of the supplier to delete", required = true)
            @PathVariable Long id) {
        supplierService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Find supplier by name", description = "Retrieve a supplier by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SupplierDTO.class))),
            @ApiResponse(responseCode = "404", description = "Supplier not found", content = @Content)
    })

    @GetMapping("/by-name/{name}")
    @RequiresRole({"ADMIN","RESPONSABLE_ACHATS"})
    public ResponseEntity<SupplierDTO> findByName(
            @Parameter(description = "Name of the supplier to find", required = true)
            @PathVariable String name) {
        try {
            SupplierDTO supplierDTO = supplierService.nameToDTO(name);
            return ResponseEntity.ok(supplierDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

}