package org.example.supplychainx.Config;

import org.example.supplychainx.Model.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Utility class for extracting Keycloak user information from JWT tokens
 */
@Component
public class KeycloakSecurityUtils {

    /**
     * Get the currently authenticated username
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            String username = jwt.getClaimAsString("preferred_username");
            if (username == null) {
                username = jwt.getClaimAsString("email");
            }
            return username;
        }
        return authentication != null ? authentication.getName() : null;
    }

    /**
     * Get the current user's email
     */
    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            return jwt.getClaimAsString("email");
        }
        return null;
    }

    /**
     * Get all roles for the current user
     */
    public static Collection<String> getCurrentUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * Check if the current user has a specific role
     */
    public static boolean hasRole(Role role) {
        Collection<String> roles = getCurrentUserRoles();
        return roles != null && roles.contains(role.name());
    }

    /**
     * Check if the current user has any of the specified roles
     */
    public static boolean hasAnyRole(Role... roles) {
        Collection<String> userRoles = getCurrentUserRoles();
        if (userRoles == null) {
            return false;
        }
        for (Role role : roles) {
            if (userRoles.contains(role.name())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the JWT token
     */
    public static Jwt getJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            return jwtAuth.getToken();
        }
        return null;
    }

    /**
     * Get a specific claim from the JWT token
     */
    public static <T> T getClaim(String claimName, Class<T> claimType) {
        Jwt jwt = getJwt();
        if (jwt != null) {
            return jwt.getClaim(claimName);
        }
        return null;
    }
}

