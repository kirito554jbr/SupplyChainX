package org.example.supplychainx.DTO.Production;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BOMDTO {

    private Long idBOM;
    private Long product_id;
    private Long material_id;
    private Integer quantity;
}
