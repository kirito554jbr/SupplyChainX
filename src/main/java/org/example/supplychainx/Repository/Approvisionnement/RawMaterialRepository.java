package org.example.supplychainx.Repository.Approvisionnement;

import org.example.supplychainx.Model.Approvisionnement.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RawMaterialRepository extends JpaRepository<RawMaterial,Long> {
    RawMaterial findByName(String name);
}
