package org.example.supplychainx.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.supplychainx.Config.KeycloakSecurityUtils;
import org.springframework.stereotype.Component;

/**
 * Aspect for audit logging of secured method access
 * Logs user access to methods annotated with @PreAuthorize
 */
@Aspect
@Component
@Slf4j
public class AuditLoggingAspect {

    @Around("@annotation(org.springframework.security.access.prepost.PreAuthorize)")
    public Object logAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        String username = KeycloakSecurityUtils.getCurrentUsername();
        String method = joinPoint.getSignature().toShortString();

        log.info("User [{}] accessing method [{}]", username, method);

        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();

            log.info("User [{}] completed method [{}] in {}ms",
                     username, method, (endTime - startTime));

            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.error("User [{}] failed method [{}] in {}ms - Error: {}",
                      username, method, (endTime - startTime), e.getMessage());
            throw e;
        }
    }
}

