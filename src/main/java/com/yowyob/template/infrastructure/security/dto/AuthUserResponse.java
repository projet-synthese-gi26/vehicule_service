package com.yowyob.template.infrastructure.security.dto;

import java.util.List;

/**
 * DTO représentant la réponse du service d'authentification externe.
 * Correspond à la réponse de GET /api/auth/me
 */
public record AuthUserResponse(
        String id,
        String username,
        String email,
        String phone,
        String firstName,
        String lastName,
        List<String> roles,
        List<String> permissions
) {
}
