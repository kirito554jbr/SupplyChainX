package org.example.supplychainx.Repository.Approvisionnement;

import org.example.supplychainx.Model.Approvisionnement.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier,Long> {

}
