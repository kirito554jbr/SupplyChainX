package org.example.supplychainx.Controller.Approvisionnement;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Approvisionnement.SupplyOrderDTO;
import org.example.supplychainx.DTO.Approvisionnement.SupplyOrderRequest;
import org.example.supplychainx.DTO.Approvisionnement.SupplyOrderResponse;
import org.example.supplychainx.Model.Approvisionnement.SupplyOrder;
import org.example.supplychainx.Service.Approvisionnement.SupplyOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supply-orders")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class SupplyOrderController {

    private final SupplyOrderService supplyOrderService;

    @GetMapping
    public ResponseEntity<List<SupplyOrderDTO>> getAllSupplyOrders() {
        List<SupplyOrderDTO> supplyOrders = supplyOrderService.findAll();
        return ResponseEntity.ok(supplyOrders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplyOrderDTO> getSupplyOrderById(@PathVariable Long id) {
        try {
            SupplyOrderDTO supplyOrder = supplyOrderService.findById(id);
            return ResponseEntity.ok(supplyOrder);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<SupplyOrderResponse> createSupplyOrder(@RequestBody SupplyOrderRequest supplyOrder) {
        SupplyOrderResponse created = supplyOrderService.save(supplyOrder);
        return ResponseEntity.status(201).body(created);
    }



    @PutMapping("/{id}")
    public ResponseEntity<SupplyOrderDTO> updateSupplyOrder(@PathVariable Long id, @RequestBody SupplyOrderDTO supplyOrder) {
        SupplyOrderDTO updated = supplyOrderService.update(id, supplyOrder);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupplyOrder(@PathVariable Long id) {
        supplyOrderService.findById(id);
        return ResponseEntity.ok("success");
    }
}
