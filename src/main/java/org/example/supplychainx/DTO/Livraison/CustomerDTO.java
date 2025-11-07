package org.example.supplychainx.DTO.Livraison;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.supplychainx.Model.Livraison.Order;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Customer data transfer object")
public class CustomerDTO {
//    private Long idCustomer;

    @Schema(description = "Name of the customer", example = "John Doe", required = true)
    private String name;

    @Schema(description = "Customer's address", example = "123 Main Street", required = true)
    private String adress;

    @Schema(description = "City where the customer is located", example = "Casablanca", required = true)
    private String city;

    @Schema(description = "List of associated orders (optional)")
    private List<OrderDTO> orders;
}
