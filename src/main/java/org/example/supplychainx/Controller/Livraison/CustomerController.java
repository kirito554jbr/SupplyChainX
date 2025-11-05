package org.example.supplychainx.Controller.Livraison;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Livraison.CustomerDTO;
import org.example.supplychainx.Service.Livraison.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@AllArgsConstructor
public class CustomerController {
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> findAll() {
        List<CustomerDTO> customers = customerService.findAll();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> findById(@PathVariable Long id) {
        CustomerDTO customer = customerService.findById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<CustomerDTO> findByName(@PathVariable String name) {
        CustomerDTO customer = customerService.findByName(name);
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> save(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO savedCustomer = customerService.save(customerDTO);
        return ResponseEntity.status(201).body(savedCustomer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> update(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        CustomerDTO updatedCustomer = customerService.update(id, customerDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        customerService.deleteById(id);
        return ResponseEntity.ok("success");
    }
}
