package org.example.supplychainx.DTO.Production;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.supplychainx.Model.Production.Product;
import org.example.supplychainx.Model.Production.StatusProduction;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ProductionOrderDTO {
    private Long idProductionOrder;
    private Long product_id;
    private Integer quantity;
    private StatusProduction status;
    private LocalDate startDate;
    private LocalDate endDate;
}
