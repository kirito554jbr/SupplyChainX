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
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSupplier;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String contact;
    @Column(nullable = false)
    private Double rating;
    @Column(nullable = false)
    private Integer leadTime;
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<SupplyOrder> supplyOrders;
}
