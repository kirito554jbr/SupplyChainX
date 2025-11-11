package org.example.supplychainx.context;

import org.example.supplychainx.Model.Role;

public class UserContext {
    private static final ThreadLocal<String> currentUser = new ThreadLocal<>();
    private static final ThreadLocal<Role> currentRole = new ThreadLocal<>();


    public static void setCurrentUser(String username, Role role) {
        currentUser.set(username);
        currentRole.set(role);
    }

    public static String getCurrentUser() {
        return currentUser.get();
    }

    public static Role getCurrentRole() {
        return currentRole.get();
    }

    public static void clear() {
        currentUser.remove();
        currentRole.remove();
    }
}
