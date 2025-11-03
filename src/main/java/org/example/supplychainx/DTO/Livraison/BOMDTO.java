package org.example.supplychainx.DTO.Livraison;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.supplychainx.Model.Approvisionnement.RawMaterial;
import org.example.supplychainx.Model.Livraison.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BOMDTO {

    private Long idBOM;
    private Long product_id;
    private Long material_id;
    private Integer quantity;
}
