package org.example.supplychainx.Repository.Production;

import org.example.supplychainx.Model.Production.ProductionOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionOrderRepository extends JpaRepository<ProductionOrder,Long> {
}
