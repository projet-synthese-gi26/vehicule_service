package com.yowyob.template.infrastructure.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Token d'authentification JWT personnalisé.
 * Utilisé pour stocker les informations de l'utilisateur authentifié dans le contexte de sécurité.
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthenticatedUser principal;
    private final String token;

    public JwtAuthenticationToken(AuthenticatedUser principal, String token,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.token = token;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public AuthenticatedUser getPrincipal() {
        return principal;
    }
}
