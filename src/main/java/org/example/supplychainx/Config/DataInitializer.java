package org.example.supplychainx.Config;

import lombok.AllArgsConstructor;
import org.example.supplychainx.Model.Role;
import org.example.supplychainx.Model.User;
import org.example.supplychainx.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Initializes the database with default users for testing
 * This runs once on application startup
 */
@Component
@AllArgsConstructor
@Order(1)
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("=".repeat(80));
        log.info("🔍 DataInitializer is starting...");
        log.info("🔍 Database connection test: Starting data initialization");
        System.out.println("=".repeat(80));
        System.out.println("🔍 DataInitializer is starting...");
        System.out.println("🔍 Database connection test: Starting data initialization");

        try {
            log.info("Checking UserRepository connection...");
            long userCount = userRepository.count();
            log.info("Current user count in database: {}", userCount);
            System.out.println("Current user count in database: " + userCount);

            // Check if admin user already exists
            if (userRepository.findByEmail("admin@test.com") == null) {
                User admin = new User();
                admin.setFirstName("Admin");
                admin.setLastName("User");
                admin.setEmail("admin@test.com");
                admin.setPassword(passwordEncoder.encode("0000"));
                admin.setRole(Role.ADMIN);
                admin.setEnabled(true);
                userRepository.save(admin);
                log.info("✅ Admin user created: admin@test.com / 0000");
                System.out.println("✅ Admin user created: admin@test.com / 0000");
            } else {
                log.info("ℹ️ Admin user already exists");
                System.out.println("ℹ️ Admin user already exists");
            }

            // Create additional test users for different roles
            if (userRepository.findByEmail("gestionnaire@test.com") == null) {
                User gestionnaire = new User();
                gestionnaire.setFirstName("Gestionnaire");
                gestionnaire.setLastName("Appro");
                gestionnaire.setEmail("gestionnaire@test.com");
                gestionnaire.setPassword(passwordEncoder.encode("0000"));
                gestionnaire.setRole(Role.GESTIONNAIRE_APPROVISIONNEMENT);
                gestionnaire.setEnabled(true);
                userRepository.save(gestionnaire);
                log.info("✅ Gestionnaire user created: gestionnaire@test.com / 0000");
                System.out.println("✅ Gestionnaire user created: gestionnaire@test.com / 0000");
            } else {
                log.info("ℹ️ Gestionnaire user already exists");
                System.out.println("ℹ️ Gestionnaire user already exists");
            }

            if (userRepository.findByEmail("production@test.com") == null) {
                User production = new User();
                production.setFirstName("Chef");
                production.setLastName("Production");
                production.setEmail("production@test.com");
                production.setPassword(passwordEncoder.encode("0000"));
                production.setRole(Role.CHEF_PRODUCTION);
                production.setEnabled(true);
                userRepository.save(production);
                log.info("✅ Production user created: production@test.com / 0000");
                System.out.println("✅ Production user created: production@test.com / 0000");
            } else {
                log.info("ℹ️ Production user already exists");
                System.out.println("ℹ️ Production user already exists");
            }

            if (userRepository.findByEmail("logistique@test.com") == null) {
                User logistique = new User();
                logistique.setFirstName("Responsable");
                logistique.setLastName("Logistique");
                logistique.setEmail("logistique@test.com");
                logistique.setPassword(passwordEncoder.encode("0000"));
                logistique.setRole(Role.RESPONSABLE_LOGISTIQUE);
                logistique.setEnabled(true);
                userRepository.save(logistique);
                log.info("✅ Logistique user created: logistique@test.com / 0000");
                System.out.println("✅ Logistique user created: logistique@test.com / 0000");
            } else {
                log.info("ℹ️ Logistique user already exists");
                System.out.println("ℹ️ Logistique user already exists");
            }

            long finalUserCount = userRepository.count();
            log.info("🚀 Database initialization complete! Total users: {}", finalUserCount);
            System.out.println("🚀 Database initialization complete! Total users: " + finalUserCount);
            log.info("=".repeat(80));
            System.out.println("=".repeat(80));
        } catch (Exception e) {
            log.error("❌ DataInitializer failed with error: {}", e.getMessage(), e);
            System.err.println("❌ DataInitializer failed with error: " + e.getMessage());
            e.printStackTrace();
            // Don't throw the exception to allow the app to continue running
            log.error("DataInitializer failed but application will continue");
        }
    }
}

