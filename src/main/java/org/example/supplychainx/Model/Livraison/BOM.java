package org.example.supplychainx.Model.Livraison;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.supplychainx.Model.Approvisionnement.RawMaterial;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "boms")
public class BOM {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBOM;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idProduct")
    @JsonBackReference
    private Product product;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idMaterial")
    @JsonBackReference
    private RawMaterial material;
    private Integer quantity;
}
