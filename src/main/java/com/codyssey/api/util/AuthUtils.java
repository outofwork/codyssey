package com.codyssey.api.util;

import com.codyssey.api.security.JwtAuthenticationDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility class for authentication-related operations
 * <p>
 * Provides convenient methods to access current user information
 * from the security context.
 */
public class AuthUtils {

    /**
     * Get current authenticated username
     *
     * @return current username or null if not authenticated
     */
    public static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : null;
    }

    /**
     * Get current authenticated user ID
     *
     * @return current user ID or null if not authenticated
     */
    public static String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getDetails() instanceof JwtAuthenticationDetails details) {
            return details.getUserId();
        }
        return null;
    }

    /**
     * Check if current user is authenticated
     *
     * @return true if authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated();
    }

    /**
     * Check if current user has a specific role
     *
     * @param role the role to check
     * @return true if user has the role, false otherwise
     */
    public static boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return auth.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals(role));
        }
        return false;
    }

    /**
     * Check if current user has any of the specified roles
     *
     * @param roles the roles to check
     * @return true if user has any of the roles, false otherwise
     */
    public static boolean hasAnyRole(String... roles) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            for (String role : roles) {
                if (auth.getAuthorities().stream()
                        .anyMatch(authority -> authority.getAuthority().equals(role))) {
                    return true;
                }
            }
        }
        return false;
    }
}
