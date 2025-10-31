package org.example.supplychainx.Controller.Approvisionnement;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Approvisionnement.RawMaterialDTO;
import org.example.supplychainx.DTO.Approvisionnement.SupplierDTO;
import org.example.supplychainx.Service.Approvisionnement.SupplierService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;


    @GetMapping
    public List<SupplierDTO> getAllSuppliers() {
        return supplierService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> getSupplierById(@PathVariable Long id) {
        try {
            SupplierDTO supplier = supplierService.findById(id);
            return ResponseEntity.ok(supplier);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<SupplierDTO> createSupplier(@RequestBody SupplierDTO supplier) {
        SupplierDTO created = supplierService.save(supplier);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO> updateSupplier(@PathVariable Long id, @RequestBody SupplierDTO supplier) {
        SupplierDTO updated = supplierService.update(id, supplier);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<SupplierDTO> findByName(@PathVariable String name) {
        try {
            SupplierDTO supplierDTO = supplierService.nameToDTO(name);
            return ResponseEntity.ok(supplierDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

}