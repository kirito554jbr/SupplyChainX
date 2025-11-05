package org.example.supplychainx.Repository.Livraison;

import org.example.supplychainx.Model.Livraison.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Customer findByName(String name);
}
