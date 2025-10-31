package org.example.supplychainx.Controller.Approvisionnement;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Approvisionnement.SupplyOrderDTO;
import org.example.supplychainx.Model.Approvisionnement.SupplyOrder;
import org.example.supplychainx.Service.Approvisionnement.SupplyOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("/api/supply-orders")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class SupplyOrderController {

    private final SupplyOrderService supplyOrderService;

    @PostMapping
    public ResponseEntity<SupplyOrderDTO> createSupplyOrder(@RequestBody SupplyOrderDTO supplyOrder) {
        SupplyOrderDTO created = supplyOrderService.save(supplyOrder);
        return ResponseEntity.status(201).body(created);
    }
}
