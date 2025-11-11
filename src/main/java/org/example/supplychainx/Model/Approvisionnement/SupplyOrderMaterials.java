package org.example.supplychainx.Model.Approvisionnement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "supplyMaterials")
public class SupplyOrderMaterials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSupplyMaterial;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "id_supplier")
    private SupplyOrder supplyOrder;

    @ManyToOne
    @JoinColumn(name = "id_material")
    private RawMaterial rawMaterial;

}
