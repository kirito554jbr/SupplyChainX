package org.example.supplychainx.Controller.Livraison;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Livraison.CustomerDTO;
import org.example.supplychainx.Service.Livraison.CustomerService;
import org.example.supplychainx.annotation.RequiresRole;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@AllArgsConstructor
@Tag(name = "Customer Management", description = "APIs for managing customers")
public class CustomerController {
    private CustomerService customerService;

    @Operation(summary = "Get all customers", description = "Retrieve a list of all customers")
    @ApiResponse(responseCode = "200", description = "List of customers retrieved successfully")
    @GetMapping
    @RequiresRole({"ADMIN","GESTIONNAIRE_COMMERCIAL"})
    public ResponseEntity<List<CustomerDTO>> findAll() {
        List<CustomerDTO> customers = customerService.findAll();
        return ResponseEntity.ok(customers);
    }

    @Operation(summary = "Get customer by ID", description = "Retrieve a specific customer by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> findById(
            @Parameter(description = "ID of the customer to retrieve", required = true)
            @PathVariable Long id) {
        CustomerDTO customer = customerService.findById(id);
        return ResponseEntity.ok(customer);
    }

    @Operation(summary = "Find customer by name", description = "Retrieve a customer by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    @GetMapping("/by-name/{name}")
    @RequiresRole({"ADMIN","GESTIONNAIRE_COMMERCIAL"})
    public ResponseEntity<CustomerDTO> findByName(
            @Parameter(description = "Name of the customer to find", required = true)
            @PathVariable String name) {
        CustomerDTO customer = customerService.findByName(name);
        return ResponseEntity.ok(customer);
    }

    @Operation(summary = "Create new customer", description = "Create a new customer in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    @RequiresRole({"ADMIN","GESTIONNAIRE_COMMERCIAL"})
    public ResponseEntity<CustomerDTO> save(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Customer details to create", required = true,
                    content = @Content(schema = @Schema(implementation = CustomerDTO.class)))
            @RequestBody CustomerDTO customerDTO) {
        CustomerDTO savedCustomer = customerService.save(customerDTO);
        return ResponseEntity.status(201).body(savedCustomer);
    }

    @Operation(summary = "Update customer", description = "Update an existing customer's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDTO.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    @PutMapping("/{id}")
    @RequiresRole({"ADMIN","GESTIONNAIRE_COMMERCIAL"})
    public ResponseEntity<CustomerDTO> update(
            @Parameter(description = "ID of the customer to update", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated customer details", required = true,
                    content = @Content(schema = @Schema(implementation = CustomerDTO.class)))
            @RequestBody CustomerDTO customerDTO) {
        CustomerDTO updatedCustomer = customerService.update(id, customerDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @Operation(summary = "Delete customer", description = "Delete a customer from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    @RequiresRole({"ADMIN","GESTIONNAIRE_COMMERCIAL"})
    public ResponseEntity<String> delete(
            @Parameter(description = "ID of the customer to delete", required = true)
            @PathVariable Long id) {
        customerService.deleteById(id);
        return ResponseEntity.ok("success");
    }
}
