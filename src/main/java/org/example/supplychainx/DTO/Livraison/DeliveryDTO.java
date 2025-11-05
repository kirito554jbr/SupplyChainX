package org.example.supplychainx.DTO.Livraison;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.supplychainx.Model.Livraison.Order;
import org.example.supplychainx.Model.Livraison.StatusDelivery;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDTO {
    private Long idDelivery;
    private Long order_id;
    private String vehicle;
    private String driver;
    private StatusDelivery status;
    private LocalDate deliveryDate;
    private Double cost;
}
