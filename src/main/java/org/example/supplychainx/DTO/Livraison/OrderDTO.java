package org.example.supplychainx.DTO.Livraison;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.supplychainx.Model.Livraison.Delivery;
import org.example.supplychainx.Model.Livraison.StatusOrder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long idOrder;
    private Long customerName_id;
    private Long product_id;
    private Integer quantity;
    private StatusOrder status;
//    private List<Delivery> deliveries;
}
