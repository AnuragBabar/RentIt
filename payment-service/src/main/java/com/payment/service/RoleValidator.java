package com.payment.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RoleValidator {

	public static void requireRole(
            HttpServletRequest request,
            String requiredRole) {

        Object roleObj = request.getAttribute("role");

        if (roleObj == null) {
            throw new RuntimeException("Role not found in JWT token");
        }

        String role = roleObj.toString();

        if (!role.equals(requiredRole)) {
            throw new RuntimeException(
                "Access denied. Required role: "
                + requiredRole
                + ", but found: "
                + role
            );
        }
    }
}
