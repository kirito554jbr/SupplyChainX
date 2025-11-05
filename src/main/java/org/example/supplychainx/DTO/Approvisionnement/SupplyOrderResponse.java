package org.example.supplychainx.DTO.Approvisionnement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.supplychainx.Model.Approvisionnement.StatusSupply;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplyOrderResponse {
    private Long id_order;
    private LocalDate order_date;
    private StatusSupply status;
    private SupplierInfo supplier;
    private List<RawMaterialInfo> rawMaterials;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SupplierInfo {
        private Long supplierId;
        private String name;
        private String contact;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RawMaterialInfo {
        private Long idMaterial;
        private String name;
        private int quantity;
    }
}
