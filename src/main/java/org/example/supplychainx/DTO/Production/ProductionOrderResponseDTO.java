package org.example.supplychainx.DTO.Production;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.supplychainx.Model.Production.StatusProduction;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ProductionOrderResponseDTO {
    private Long idProductionOrder;
    private Integer quantity;
    private StatusProduction status;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProductResponseDTO product;
}
