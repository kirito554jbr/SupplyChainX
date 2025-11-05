package org.example.supplychainx.DTO.Approvisionnement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.supplychainx.Model.Approvisionnement.StatusSupply;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplyOrderDTO {

    private Long idOrder;
    private String supplierName;
    private LocalDate orderDate;
    private StatusSupply status;
}
