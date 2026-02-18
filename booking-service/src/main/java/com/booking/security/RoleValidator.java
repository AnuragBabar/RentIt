package com.booking.security;

import jakarta.servlet.http.HttpServletRequest;

public class RoleValidator {

    public static void requireRole(
            HttpServletRequest request,
            String requiredRole) {

        String role = (String) request.getAttribute("role");

        if (role == null || !role.equals(requiredRole)) {
            throw new RuntimeException(
                "Access denied for role: " + role
            );
        }
    }
}
