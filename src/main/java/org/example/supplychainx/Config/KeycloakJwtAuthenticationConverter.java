package org.example.supplychainx.Config;

import org.example.supplychainx.Model.Role;
import org.example.supplychainx.context.UserContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                defaultGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()
        ).collect(Collectors.toList());

        // Extract username and roles for UserContext
        String username = jwt.getClaimAsString("preferred_username");
        if (username == null) {
            username = jwt.getClaimAsString("email");
        }

        // Extract the first matching role from our Role enum
        Role userRole = extractUserRole(authorities);

        // Set the user context
        if (username != null && userRole != null) {
            UserContext.setCurrentUser(username, userRole);
        }

        return new JwtAuthenticationToken(jwt, authorities, username);
    }

    private Collection<GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        Collection<String> roles = new ArrayList<>();

        if (realmAccess != null && realmAccess.get("roles") instanceof Collection<?>) {
            @SuppressWarnings("unchecked")
            Collection<String> realmRoles = (Collection<String>) realmAccess.get("roles");
            roles = realmRoles;
        }

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());
    }

    private Role extractUserRole(Collection<GrantedAuthority> authorities) {
        // Try to match authorities with our Role enum
        for (GrantedAuthority authority : authorities) {
            String authorityName = authority.getAuthority();
            if (authorityName.startsWith("ROLE_")) {
                String roleName = authorityName.substring(5); // Remove "ROLE_" prefix
                try {
                    return Role.valueOf(roleName);
                } catch (IllegalArgumentException e) {
                    // Role doesn't match our enum, continue
                }
            }
        }
        return null;
    }
}

