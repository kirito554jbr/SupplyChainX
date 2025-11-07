package org.example.supplychainx.DTO.Livraison;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.supplychainx.Model.Livraison.Delivery;
import org.example.supplychainx.Model.Livraison.StatusOrder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Order data transfer object")
public class OrderDTO {
//    private Long idOrder;

    @Schema(description = "ID of the customer placing the order", example = "1", required = true)
    private Long customer_id;

    @Schema(description = "ID of the product being ordered", example = "1", required = true)
    private Long product_id;

    @Schema(description = "Quantity of products ordered", example = "100", required = true)
    private Integer quantity;

    @Schema(description = "Current status of the order", example = "PENDING", required = true,
            allowableValues = {"PENDING", "PROCESSING", "COMPLETED", "CANCELLED"})
    private StatusOrder status;
//    private List<Delivery> deliveries;
}
