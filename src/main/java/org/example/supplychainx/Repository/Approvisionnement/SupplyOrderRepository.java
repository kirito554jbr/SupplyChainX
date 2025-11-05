package org.example.supplychainx.Repository.Approvisionnement;


import org.example.supplychainx.Model.Approvisionnement.SupplyOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplyOrderRepository extends JpaRepository<SupplyOrder,Long> {
}
