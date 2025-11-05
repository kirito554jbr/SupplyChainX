package org.example.supplychainx.Repository.Livraison;

import org.example.supplychainx.Model.Livraison.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery,Long> {
}
