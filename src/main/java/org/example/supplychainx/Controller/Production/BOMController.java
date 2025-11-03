package org.example.supplychainx.Controller.Production;


import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Production.BOMDTO;
import org.example.supplychainx.Service.Production.BOMService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boms")
@AllArgsConstructor
public class BOMController {

    private BOMService BOMService;

    @GetMapping
    public ResponseEntity<List<BOMDTO>> getAllBOMs() {
        java.util.List<BOMDTO> boms = BOMService.getAllBOMs();
        return ResponseEntity.ok(boms);
    }


    @GetMapping("/{id}")
    public ResponseEntity<BOMDTO> getBOMById(@PathVariable Long id) {
        try {
            BOMDTO bomDTO = BOMService.getBOMById(id);
            return ResponseEntity.ok(bomDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();

        }
    }

    @PostMapping
    public ResponseEntity<BOMDTO> createBOM(@RequestBody BOMDTO bomDTO) {
//        System.out.println("Received BOMDTO: " + bomDTO);
        BOMDTO created = BOMService.createBOM(bomDTO);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BOMDTO> updateBOM(@PathVariable Long id, @RequestBody BOMDTO bomDTO) {
        BOMDTO updated = BOMService.updateBOM(id, bomDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBOM(@PathVariable Long id) {
        BOMService.deleteBOM(id);
        return ResponseEntity.ok("success");
    }
}
