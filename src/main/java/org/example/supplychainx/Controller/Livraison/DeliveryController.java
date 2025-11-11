package org.example.supplychainx.Controller.Livraison;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Livraison.DeliveryDTO;
import org.example.supplychainx.Service.Livraison.DeliveryService;
import org.example.supplychainx.annotation.RequiresRole;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@AllArgsConstructor
@Tag(name = "Delivery Management", description = "APIs for managing product deliveries to customers")
public class DeliveryController {
    private DeliveryService deliveryService;

    @Operation(summary = "Get delivery by ID", description = "Retrieve a specific delivery by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delivery found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeliveryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Delivery not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDTO> findDeliveryById(
            @Parameter(description = "ID of the delivery to retrieve", required = true)
            @PathVariable Long id) {
        DeliveryDTO deliveryDTO = deliveryService.findById(id);
        return ResponseEntity.ok(deliveryDTO);
    }
    @Operation(summary = "Get all deliveries", description = "Retrieve a list of all deliveries")
    @ApiResponse(responseCode = "200", description = "List of deliveries retrieved successfully")
    @GetMapping
    public ResponseEntity<List<DeliveryDTO>> findAllDelivery() {
        List<DeliveryDTO> deliveryDTOS = deliveryService.findAll();
        return ResponseEntity.ok(deliveryDTOS);
    }

    @Operation(summary = "Create new delivery", description = "Create a new delivery")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Delivery created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeliveryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    @RequiresRole({"ADMIN","RESPONSABLE_LOGISTIQUE"})
    public ResponseEntity<DeliveryDTO> createDelivery(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Delivery details to create", required = true,
                    content = @Content(schema = @Schema(implementation = DeliveryDTO.class)))
            @RequestBody DeliveryDTO deliveryDTO) {
        DeliveryDTO createdDelivery = deliveryService.save(deliveryDTO);
        return ResponseEntity.status(201).body(createdDelivery);
    }

    @Operation(summary = "Update delivery", description = "Update an existing delivery's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delivery updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeliveryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Delivery not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<DeliveryDTO> updateDelivery(
            @Parameter(description = "ID of the delivery to update", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated delivery details", required = true,
                    content = @Content(schema = @Schema(implementation = DeliveryDTO.class)))
            @RequestBody DeliveryDTO deliveryDTO) {
        DeliveryDTO updatedDelivery = deliveryService.update(id, deliveryDTO);
        return ResponseEntity.ok(updatedDelivery);
    }

    @Operation(summary = "Delete delivery", description = "Delete a delivery from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delivery deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Delivery not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDelivery(
            @Parameter(description = "ID of the delivery to delete", required = true)
            @PathVariable Long id) {
        deliveryService.delete(id);
        return ResponseEntity.ok("success");
    }
}
