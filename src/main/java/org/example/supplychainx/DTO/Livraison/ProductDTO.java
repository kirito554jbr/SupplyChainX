package org.example.supplychainx.DTO.Livraison;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.supplychainx.Model.Livraison.BOM;
import org.example.supplychainx.Model.Livraison.ProductionOrder;
import org.example.supplychainx.Model.Production.Order;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long idProduct;
    private String name;
    private Integer productionTime;
    private Double cost;
    private Integer stock;
    private List<BOMDTO> boms;
//    private List<Order> orders;
//    private List<ProductionOrder> productionOrders;
}
