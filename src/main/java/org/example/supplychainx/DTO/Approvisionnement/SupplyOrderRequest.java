package org.example.supplychainx.DTO.Approvisionnement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SupplyOrderRequest {
    private LocalDate date;
    private Long supplierId;
    private List<RawMaterialQuantity> rawMaterials;

    // nested class or a separate one
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RawMaterialQuantity {
        private Long rawMaterialId;
        private int quantity;

        public void setRawMaterialId(Long rawMaterialId) { this.rawMaterialId = rawMaterialId; }

        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}