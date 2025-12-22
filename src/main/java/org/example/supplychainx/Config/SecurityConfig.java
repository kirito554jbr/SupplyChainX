package org.example.supplychainx.Config;

import lombok.AllArgsConstructor;
import org.example.supplychainx.Service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private CustomUserDetailsService userDetailsService;
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - Authentication & Registration
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/users/register").permitAll()

                        // User Management - /api/users
                        .requestMatchers("/api/users/**").hasAnyRole("ADMIN", "GESTIONNAIRE_APPROVISIONNEMENT", "RESPONSABLE_ACHATS", "SUPERVISEUR_LOGISTIQUE", "CHEF_PRODUCTION", "PLANIFICATEUR", "SUPERVISEUR_PRODUCTION", "GESTIONNAIRE_COMMERCIAL", "RESPONSABLE_LOGISTIQUE", "SUPERVISEUR_LIVRAISONS")

                        // Raw Materials - /api/rawMaterials
                        .requestMatchers("/api/rawMaterials/filter/low-stock").hasAnyRole("ADMIN", "SUPERVISEUR_LOGISTIQUE")
                        .requestMatchers("/api/rawMaterials/*/suppliers/*").hasAnyRole("ADMIN", "GESTIONNAIRE_APPROVISIONNEMENT")
                        .requestMatchers("/api/rawMaterials").hasAnyRole("ADMIN", "SUPERVISEUR_LOGISTIQUE", "PLANIFICATEUR", "GESTIONNAIRE_APPROVISIONNEMENT")
                        .requestMatchers("/api/rawMaterials/*").hasAnyRole("ADMIN", "SUPERVISEUR_LOGISTIQUE", "PLANIFICATEUR", "GESTIONNAIRE_APPROVISIONNEMENT")

                        // Suppliers - /api/suppliers
                        .requestMatchers("/api/suppliers").hasAnyRole("ADMIN", "SUPERVISEUR_LOGISTIQUE", "GESTIONNAIRE_APPROVISIONNEMENT")
                        .requestMatchers("/api/suppliers/*").hasAnyRole("ADMIN", "SUPERVISEUR_LOGISTIQUE", "GESTIONNAIRE_APPROVISIONNEMENT")

                        // Supply Orders - /api/supply-orders
                        .requestMatchers("/api/supply-orders").hasAnyRole("ADMIN", "SUPERVISEUR_LOGISTIQUE", "RESPONSABLE_ACHATS")
                        .requestMatchers("/api/supply-orders/*").hasAnyRole("ADMIN", "SUPERVISEUR_LOGISTIQUE", "RESPONSABLE_ACHATS")

                        // Customers - /api/customers
                        .requestMatchers("/api/customers").hasAnyRole("ADMIN", "GESTIONNAIRE_COMMERCIAL")
                        .requestMatchers("/api/customers/*").hasAnyRole("ADMIN", "GESTIONNAIRE_COMMERCIAL")
                        .requestMatchers("/api/customers/by-name/*").hasAnyRole("ADMIN", "GESTIONNAIRE_COMMERCIAL")

                        // Deliveries - /api/deliveries
                        .requestMatchers("/api/deliveries").hasAnyRole("ADMIN", "RESPONSABLE_LOGISTIQUE", "SUPERVISEUR_LIVRAISONS")
                        .requestMatchers("/api/deliveries/*").hasAnyRole("ADMIN", "RESPONSABLE_LOGISTIQUE", "SUPERVISEUR_LIVRAISONS")

                        // Orders - /api/orders
                        .requestMatchers("/api/orders").hasAnyRole("ADMIN", "SUPERVISEUR_LIVRAISONS", "GESTIONNAIRE_COMMERCIAL")
                        .requestMatchers("/api/orders/*").hasAnyRole("ADMIN", "SUPERVISEUR_LIVRAISONS", "GESTIONNAIRE_COMMERCIAL")

                        // Products - /api/products
                        .requestMatchers("/api/products").hasAnyRole("ADMIN", "SUPERVISEUR_PRODUCTION", "CHEF_PRODUCTION", "PLANIFICATEUR")
                        .requestMatchers("/api/products/*").hasAnyRole("ADMIN", "SUPERVISEUR_PRODUCTION", "CHEF_PRODUCTION", "PLANIFICATEUR")

                        // Bill of Materials (BOM) - /api/boms
                        .requestMatchers("/api/boms").hasAnyRole("ADMIN", "CHEF_PRODUCTION", "SUPERVISEUR_PRODUCTION", "PLANIFICATEUR")
                        .requestMatchers("/api/boms/*").hasAnyRole("ADMIN", "CHEF_PRODUCTION", "SUPERVISEUR_PRODUCTION", "PLANIFICATEUR")

                        // Production Orders - /api/productionOrders
                        .requestMatchers("/api/productionOrders").hasAnyRole("ADMIN", "SUPERVISEUR_PRODUCTION", "CHEF_PRODUCTION")
                        .requestMatchers("/api/productionOrders/*").hasAnyRole("ADMIN", "SUPERVISEUR_PRODUCTION", "CHEF_PRODUCTION")

                        // Swagger UI and API Docs
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()

                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
