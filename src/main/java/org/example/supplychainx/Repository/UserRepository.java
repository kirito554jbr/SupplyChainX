package org.example.supplychainx.Repository;

import org.example.supplychainx.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByLastName(String lastName);
    User findByEmail(String email);
    Optional<User> findOptionalByEmail(String email);
}
