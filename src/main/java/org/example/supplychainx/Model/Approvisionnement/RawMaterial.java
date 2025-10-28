package org.example.supplychainx.Model.Approvisionnement;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idSupplier")
    @JsonBackReference
    private Supplier supplier;
}
