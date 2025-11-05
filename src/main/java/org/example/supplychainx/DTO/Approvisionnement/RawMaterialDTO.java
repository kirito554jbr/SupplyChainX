package org.example.supplychainx.DTO.Approvisionnement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RawMaterialDTO {

    private Long idRawMaterial;
    private String name;
    private Integer stock;
    private Integer MinStock;
    private String unit;
//    private Long idSupplier;
//    private String supplierName;
    private List<String> suppliers;
//    private List<Integer> boms;
}

