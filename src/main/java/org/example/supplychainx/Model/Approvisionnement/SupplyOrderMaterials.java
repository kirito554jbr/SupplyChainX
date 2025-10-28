package org.example.supplychainx.Model.Approvisionnement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "supplyOrderMaterials")
public class SupplyOrderMaterials {

    @EmbeddedId
    private SupplyOrderMaterialsId id;

    @ManyToOne
    @MapsId("idOrder")
    @JoinColumn(name = "idOrder")
    private SupplyOrder supplyOrder;

    @ManyToOne
    @MapsId("idMaterial")
    @JoinColumn(name = "idMaterial")
    private RawMaterial rawMaterial;

    @Column(nullable = false)
    private Integer quantity;
}
