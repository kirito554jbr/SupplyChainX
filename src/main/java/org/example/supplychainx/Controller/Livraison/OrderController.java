package org.example.supplychainx.Controller.Livraison;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Livraison.OrderDTO;
import org.example.supplychainx.Service.Livraison.OrderService;
import org.example.supplychainx.annotation.RequiresRole;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
@Tag(name = "Order Management", description = "APIs for managing customer orders")
public class OrderController {
    private OrderService orderService;

    @Operation(summary = "Get all orders", description = "Retrieve a list of all customer orders")
    @ApiResponse(responseCode = "200", description = "List of orders retrieved successfully")
    @GetMapping
    @RequiresRole({"ADMIN","SUPERVISEUR_LIVRAISONS"})
    public ResponseEntity<List<OrderDTO>> findAll() {
        List<OrderDTO> orders = orderService.findAll();
        return  ResponseEntity.ok(orders);
    }

    @Operation(summary = "Get order by ID", description = "Retrieve a specific order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> findById(
            @Parameter(description = "ID of the order to retrieve", required = true)
            @PathVariable Long id) {
        OrderDTO order = orderService.findById(id);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Create new order", description = "Create a new customer order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    @RequiresRole({"ADMIN","GESTIONNAIRE_COMMERCIAL"})
    public  ResponseEntity<OrderDTO> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Order details to create", required = true,
                    content = @Content(schema = @Schema(implementation = OrderDTO.class)))
            @RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.save(orderDTO);
        return ResponseEntity.status(201).body(createdOrder);
    }

    @Operation(summary = "Update order", description = "Update an existing order's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @PutMapping("/{id}")
    @RequiresRole({"ADMIN","GESTIONNAIRE_COMMERCIAL"})
    public ResponseEntity<OrderDTO> update(
            @Parameter(description = "ID of the order to update", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated order details", required = true,
                    content = @Content(schema = @Schema(implementation = OrderDTO.class)))
            @RequestBody OrderDTO orderDTO) {
        OrderDTO updatedOrder = orderService.update(id, orderDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    @Operation(summary = "Delete order", description = "Delete an order from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    @RequiresRole({"ADMIN","GESTIONNAIRE_COMMERCIAL"})
    public ResponseEntity<String> delete(
            @Parameter(description = "ID of the order to delete", required = true)
            @PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.ok("success");
    }

}
