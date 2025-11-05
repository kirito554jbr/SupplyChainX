package org.example.supplychainx.DTO.Livraison;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.supplychainx.Model.Livraison.Order;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private Long idCustomer;
    private String name;
    private String adress;
    private String city;
    private Long order_id;
}
