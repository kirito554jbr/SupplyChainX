package org.example.supplychainx.Model.Approvisionnement;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "supplyOrders")
public class SupplyOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long idOrder;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idSupplier")
    @JsonBackReference
    private Supplier supplier;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idMaterial")
    @JsonBackReference
    private RawMaterial material;
    private String orderDate;
    private StatusSupply status;

}
