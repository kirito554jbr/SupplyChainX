package org.example.supplychainx.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.supplychainx.context.UserContext;
import org.example.supplychainx.Model.Role;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Extract user info from request headers
        String userEmail = request.getHeader("X-User-Email");
        String roleHeader = request.getHeader("X-User-Role");

        if (userEmail != null && roleHeader != null) {
            try {
                Role role = Role.valueOf(roleHeader.toUpperCase());
                UserContext.setCurrentUser(userEmail, role);
            } catch (IllegalArgumentException e) {
                UserContext.clear();
            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
}
