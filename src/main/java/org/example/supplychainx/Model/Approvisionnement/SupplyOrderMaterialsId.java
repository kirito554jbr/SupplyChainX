package org.example.supplychainx.Model.Approvisionnement;

import jakarta.persistence.Embeddable;

@Embeddable
public class SupplyOrderMaterialsId {
    private Long idOrder;
    private Long idMaterial;
}
