package org.example.supplychainx.DTO.Livraison;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Customer data transfer object")
public class CustomerDTO {

    @Schema(description = "Name of the customer", example = "John Doe")
    private String name;

    @Schema(description = "Customer's address", example = "123 Main Street")
    private String adress;

    @Schema(description = "City where the customer is located", example = "Casablanca")
    private String city;

    @Schema(description = "List of associated orders (optional)")
    private List<OrderDTO> orders;
}
