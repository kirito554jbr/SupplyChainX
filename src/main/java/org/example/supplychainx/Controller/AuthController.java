package org.example.supplychainx.Controller;

import lombok.RequiredArgsConstructor;
import org.example.supplychainx.Config.KeycloakSecurityUtils;
import org.example.supplychainx.Model.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller to demonstrate Keycloak authentication and authorization
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    /**
     * Public endpoint - no authentication required
     */
    @GetMapping("/public/health")
    public ResponseEntity<Map<String, String>> publicHealth() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Public endpoint - no authentication required");
        return ResponseEntity.ok(response);
    }

    /**
     * Get current user info - requires authentication
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", KeycloakSecurityUtils.getCurrentUsername());
        userInfo.put("email", KeycloakSecurityUtils.getCurrentUserEmail());
        userInfo.put("roles", KeycloakSecurityUtils.getCurrentUserRoles());
        userInfo.put("sub", jwt.getSubject());
        userInfo.put("given_name", jwt.getClaimAsString("given_name"));
        userInfo.put("family_name", jwt.getClaimAsString("family_name"));
        return ResponseEntity.ok(userInfo);
    }

    /**
     * Admin-only endpoint
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> adminEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome, Admin!");
        response.put("user", KeycloakSecurityUtils.getCurrentUsername());
        return ResponseEntity.ok(response);
    }

    /**
     * Production management endpoint - accessible by production roles
     */
    @GetMapping("/production")
    @PreAuthorize("hasAnyRole('ADMIN', 'CHEF_PRODUCTION', 'SUPERVISEUR_PRODUCTION', 'PLANIFICATEUR')")
    public ResponseEntity<Map<String, Object>> productionEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Production Management Access");
        response.put("user", KeycloakSecurityUtils.getCurrentUsername());
        response.put("roles", KeycloakSecurityUtils.getCurrentUserRoles());
        return ResponseEntity.ok(response);
    }

    /**
     * Supply chain endpoint - accessible by supply chain roles
     */
    @GetMapping("/approvisionnement")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTIONNAIRE_APPROVISIONNEMENT', 'RESPONSABLE_ACHATS')")
    public ResponseEntity<Map<String, String>> approvisionnementEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Supply Chain Management Access");
        response.put("user", KeycloakSecurityUtils.getCurrentUsername());
        return ResponseEntity.ok(response);
    }

    /**
     * Logistics endpoint - accessible by logistics roles
     */
    @GetMapping("/logistique")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISEUR_LOGISTIQUE', 'RESPONSABLE_LOGISTIQUE', 'SUPERVISEUR_LIVRAISONS')")
    public ResponseEntity<Map<String, String>> logistiqueEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logistics Management Access");
        response.put("user", KeycloakSecurityUtils.getCurrentUsername());
        return ResponseEntity.ok(response);
    }

    /**
     * Check if user has specific role
     */
    @GetMapping("/check-role")
    public ResponseEntity<Map<String, Object>> checkRole() {
        Map<String, Object> response = new HashMap<>();
        response.put("username", KeycloakSecurityUtils.getCurrentUsername());
        response.put("isAdmin", KeycloakSecurityUtils.hasRole(Role.ADMIN));
        response.put("isChefProduction", KeycloakSecurityUtils.hasRole(Role.CHEF_PRODUCTION));
        response.put("allRoles", KeycloakSecurityUtils.getCurrentUserRoles());
        return ResponseEntity.ok(response);
    }
}

