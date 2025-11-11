package org.example.supplychainx.DTO.Approvisionnement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDTO {

    private Long idSupplier;
    private String name;
    private String contact;
    private Double rating;
    private Integer leadTime;
}
