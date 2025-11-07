package org.example.supplychainx.Repository.Production;

import org.example.supplychainx.Model.Production.BOM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BOMRepository extends JpaRepository<BOM,Long> {
}
