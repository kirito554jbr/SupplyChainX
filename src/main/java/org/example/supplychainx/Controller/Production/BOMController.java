package org.example.supplychainx.Controller.Production;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Production.BOMDTO;
import org.example.supplychainx.Service.Production.BOMService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boms")
@AllArgsConstructor
@Tag(name = "Bill of Materials (BOM)", description = "APIs for managing bill of materials for products")
public class BOMController {

    private BOMService BOMService;

    @Operation(summary = "Get all BOMs", description = "Retrieve a list of all bill of materials")
    @ApiResponse(responseCode = "200", description = "List of BOMs retrieved successfully")
    @GetMapping
    public ResponseEntity<List<BOMDTO>> getAllBOMs() {
        java.util.List<BOMDTO> boms = BOMService.getAllBOMs();
        return ResponseEntity.ok(boms);
    }


    @Operation(summary = "Get BOM by ID", description = "Retrieve a specific bill of materials by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "BOM found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BOMDTO.class))),
            @ApiResponse(responseCode = "404", description = "BOM not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BOMDTO> getBOMById(
            @Parameter(description = "ID of the BOM to retrieve", required = true)
            @PathVariable Long id) {
        try {
            BOMDTO bomDTO = BOMService.getBOMById(id);
            return ResponseEntity.ok(bomDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();

        }
    }

    @Operation(summary = "Create new BOM", description = "Create a new bill of materials entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "BOM created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BOMDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    public ResponseEntity<BOMDTO> createBOM(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "BOM details to create", required = true,
                    content = @Content(schema = @Schema(implementation = BOMDTO.class)))
            @RequestBody BOMDTO bomDTO) {
        BOMDTO created = BOMService.createBOM(bomDTO);
        return ResponseEntity.status(201).body(created);
    }

    @Operation(summary = "Update BOM", description = "Update an existing bill of materials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "BOM updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BOMDTO.class))),
            @ApiResponse(responseCode = "404", description = "BOM not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<BOMDTO> updateBOM(
            @Parameter(description = "ID of the BOM to update", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated BOM details", required = true,
                    content = @Content(schema = @Schema(implementation = BOMDTO.class)))
            @RequestBody BOMDTO bomDTO) {
        BOMDTO updated = BOMService.updateBOM(id, bomDTO);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete BOM", description = "Delete a bill of materials from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "BOM deleted successfully"),
            @ApiResponse(responseCode = "404", description = "BOM not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBOM(
            @Parameter(description = "ID of the BOM to delete", required = true)
            @PathVariable Long id) {
        BOMService.deleteBOM(id);
        return ResponseEntity.ok("success");
    }
}
