package org.example.supplychainx.DTO.Livraison;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.supplychainx.DTO.Approvisionnement.SupplyOrderRequest;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private Long idProduct;
    private String name;
    private Integer productionTime;
    private Double cost;
    private Integer stock;
//    private List<BOMsub> rawMaterials;
//
//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public static class BOMsub {
//        private Long IdBOM;
//        private String productName;
//        private String materialName;
//        private int quantity;
//    }
}
