package org.example.supplychainx.Controller.Livraison;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Livraison.OrderDTO;
import org.example.supplychainx.Service.Livraison.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDTO>> findAll() {
        List<OrderDTO> orders = orderService.findAll();
        return  ResponseEntity.ok(orders);
    }

    @GetMapping("/{id} ")
    public ResponseEntity<OrderDTO> findById(@PathVariable Long id) {
        OrderDTO order = orderService.findById(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public  ResponseEntity<OrderDTO> create(@RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.save(orderDTO);
        return ResponseEntity.status(201).body(createdOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> update(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        OrderDTO updatedOrder = orderService.update(id, orderDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.ok("success");
    }

}
