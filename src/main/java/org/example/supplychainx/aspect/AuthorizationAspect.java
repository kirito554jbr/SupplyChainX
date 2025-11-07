package org.example.supplychainx.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.supplychainx.annotation.RequiresRole;
import org.example.supplychainx.context.UserContext;
import org.example.supplychainx.Model.Role;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class AuthorizationAspect {

    @Before("@annotation(requiresRole)")
    public void checkAuthorization(JoinPoint joinPoint, RequiresRole requiresRole) {
        Role currentRole = UserContext.getCurrentRole();
        String currentUser = UserContext.getCurrentUser();

        if (currentRole == null) {
            log.warn("No user context found for method: {}", joinPoint.getSignature());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        String[] allowedRoles = requiresRole.value();
        boolean hasAccess = Arrays.stream(allowedRoles)
                .anyMatch(role -> role.equals(currentRole.name()));

        if (!hasAccess) {
            log.warn("User {} with role {} attempted to access {} but requires roles: {}",
                    currentUser, currentRole, joinPoint.getSignature(), Arrays.toString(allowedRoles));
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Access denied. Required roles: " + Arrays.toString(allowedRoles));
        }

        log.info("User {} with role {} authorized for {}", currentUser, currentRole, joinPoint.getSignature());
    }
}