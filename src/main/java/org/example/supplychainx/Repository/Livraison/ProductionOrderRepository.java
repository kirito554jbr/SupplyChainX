package org.example.supplychainx.Repository.Livraison;

import org.example.supplychainx.Model.Livraison.ProductionOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionOrderRepository extends JpaRepository<ProductionOrder,Long> {
}
