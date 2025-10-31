package org.example.supplychainx.Model.Approvisionnement;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    @JoinColumn(name = "id_supplier")
    @JsonBackReference
    private Supplier supplier;

    @OneToMany(mappedBy = "supplyOrder" , cascade = CascadeType.ALL)
    private List<SupplyOrderMaterials> supplyOrderMaterials;

    private LocalDate orderDate;
    @Enumerated(EnumType.STRING)
    private StatusSupply status;

}
