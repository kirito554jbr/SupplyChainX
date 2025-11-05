package org.example.supplychainx.DTO.Production;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
