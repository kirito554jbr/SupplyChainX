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

//    @EmbeddedId
//    private SupplyMaterialsId id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSupplyMaterial;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
//    @MapsId("idSupplier")
    @JoinColumn(name = "id_supplier")
    private SupplyOrder supplyOrder;

    @ManyToOne
//    @MapsId("idMaterial")
    @JoinColumn(name = "id_material")
    private RawMaterial rawMaterial;

}
