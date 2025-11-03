package org.example.supplychainx.Model.Livraison;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "productionOrders")
public class ProductionOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProductionOrder;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idProduct")
    @JsonBackReference
    private Product product;
    private Integer quantity;
    @Enumerated(EnumType.STRING)
    private StatusProduction status;
    private LocalDate startDate;
    private LocalDate endDate;
}
