package org.example.supplychainx.Model.Approvisionnement;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.supplychainx.Model.Production.BOM;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rawMaterials")
public class RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMaterial;
    private String name;
    private Integer stock;
    private Integer MinStock;
    private String unit;

    @ManyToMany
    @JoinTable(
            name = "material_suppliers",
            joinColumns = @JoinColumn(name = "id_supplier"),
            inverseJoinColumns = @JoinColumn(name = "id_material")
    )
    private List<Supplier> suppliers;

    @OneToMany(mappedBy = "rawMaterial", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<SupplyOrderMaterials> SupplyOrderMaterials;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<BOM> boms;
}
