package org.example.supplychainx.Controller.Approvisionnement;

import lombok.AllArgsConstructor;
import org.example.supplychainx.DTO.Approvisionnement.RawMaterialDTO;
import org.example.supplychainx.Service.Approvisionnement.RawMateialService;
import org.example.supplychainx.annotation.RequiresRole;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rawMaterials")
@AllArgsConstructor
public class RawMaterialController {

    private final RawMateialService rawMaterialService;


    @GetMapping
    @RequiresRole({"ADMIN","SUPERVISEUR_LOGISTIQUE","PLANIFICATEUR"})
    public ResponseEntity<List<RawMaterialDTO>> getRawMaterials() {
        return ResponseEntity.ok(rawMaterialService.findAllRawMaterials());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RawMaterialDTO> getRawMaterialById(@PathVariable Long id) {
        try {
            RawMaterialDTO rawMaterialDTO = rawMaterialService.findByIdRawMaterial(id);
            return ResponseEntity.ok(rawMaterialDTO);
        }catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @RequiresRole({"ADMIN","GESTIONNAIRE_APPROVISIONNEMENT"})
    public ResponseEntity<RawMaterialDTO> createRawMaterial(@RequestBody RawMaterialDTO rawMaterial) {
        RawMaterialDTO rawMaterial1 = rawMaterialService.saveRawMaterial(rawMaterial);
        return ResponseEntity.status(201).body(rawMaterial1);
    }

    @PutMapping("/{id}")
    @RequiresRole({"ADMIN","GESTIONNAIRE_APPROVISIONNEMENT"})
    public ResponseEntity<RawMaterialDTO> updateRawMaterial(@PathVariable Long id, @RequestBody RawMaterialDTO rawMaterial) {
        RawMaterialDTO rawMaterial1 = rawMaterialService.updateRawMaterial(id, rawMaterial);
        return ResponseEntity.ok(rawMaterial1);
    }

    @DeleteMapping("/{id}")
    @RequiresRole({"ADMIN","GESTIONNAIRE_APPROVISIONNEMENT"})
    public ResponseEntity<String> deleteRawMaterial(@PathVariable Long id) {
        rawMaterialService.deleteRawMaterial(id);
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/{idRawMaterial}/suppliers/{idSupplier}")
    public ResponseEntity<String> deleteOneSupplierFromRawMaterial(@PathVariable Long idRawMaterial,@PathVariable Long idSupplier) {
        rawMaterialService.deleteOneSupplierFromRawMaterial(idRawMaterial, idSupplier);
        return ResponseEntity.ok("Supplier deleted with success");
    }

    @PostMapping("/{idRawMaterial}/suppliers/{idSupplier}")
    public ResponseEntity<String> addSupplierToRawMaterial(@PathVariable Long idRawMaterial, @PathVariable Long idSupplier) {
        rawMaterialService.addSupplierToRawMaterial(idRawMaterial, idSupplier);
        return ResponseEntity.ok("Supplier added with success");
    }

    @GetMapping("/filter/low-stock")
    @RequiresRole({"ADMIN","SUPERVISEUR_LOGISTIQUE"})
    public ResponseEntity<List<RawMaterialDTO>> FiltreMaterialsLowerThanStockMin() {
        List<RawMaterialDTO> rawMaterials = rawMaterialService.getRawMaterialsWithLowStock();
        return ResponseEntity.ok(rawMaterials);
    }


}
