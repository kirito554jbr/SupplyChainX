package org.example.supplychainx.Controller.Livraison;


import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Livraison.DeliveryDTO;
import org.example.supplychainx.Service.Livraison.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@AllArgsConstructor
public class DeliveryController {
    private DeliveryService deliveryService;

    public ResponseEntity<DeliveryDTO> findDeliveryById(@PathVariable Long id) {
        DeliveryDTO deliveryDTO = deliveryService.findById(id);
        return ResponseEntity.ok(deliveryDTO);
    }

//    public ResponseEntity<DeliveryDTO> findDeliveryByOrderId(Long id) {
//        DeliveryDTO deliveryDTO = deliveryService.findByOrderId(id);
//        return ResponseEntity.ok(deliveryDTO);
//    }
    public ResponseEntity<List<DeliveryDTO>> findAllDelivery() {
        List<DeliveryDTO> deliveryDTOS = deliveryService.findAll();
        return ResponseEntity.ok(deliveryDTOS);
    }

    public ResponseEntity<DeliveryDTO> createDelivery(@RequestBody DeliveryDTO deliveryDTO) {
        DeliveryDTO createdDelivery = deliveryService.save(deliveryDTO);
        return ResponseEntity.status(201).body(createdDelivery);
    }

    public ResponseEntity<DeliveryDTO> updateDelivery(@PathVariable Long id, @RequestBody DeliveryDTO deliveryDTO) {
        DeliveryDTO updatedDelivery = deliveryService.update(id, deliveryDTO);
        return ResponseEntity.ok(updatedDelivery);
    }

    public ResponseEntity<String> deleteDelivery(@PathVariable Long id) {
        deliveryService.delete(id);
        return ResponseEntity.ok("success");
    }
}
