package org.example.supplychainx.DTO.Livraison;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.supplychainx.Model.Livraison.StatusOrder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Order data transfer object")
public class OrderDTO {

    @Schema(description = "ID of the customer placing the order", example = "1")
    private Long customer_id;

    @Schema(description = "ID of the product being ordered", example = "1")
    private Long product_id;

    @Schema(description = "Quantity of products ordered", example = "100")
    private Integer quantity;

    @Schema(description = "Current status of the order", example = "PENDING",
            allowableValues = {"PENDING", "PROCESSING", "COMPLETED", "CANCELLED"})
    private StatusOrder status;
}
