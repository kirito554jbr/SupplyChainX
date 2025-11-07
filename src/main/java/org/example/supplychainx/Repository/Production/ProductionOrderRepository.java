package org.example.supplychainx.Repository.Production;

import org.example.supplychainx.Model.Production.ProductionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionOrderRepository extends JpaRepository<ProductionOrder,Long> {
}
