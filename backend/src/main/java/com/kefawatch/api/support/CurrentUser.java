package com.kefawatch.api.support;

import com.kefawatch.security.AuthPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public final class CurrentUser {

    private CurrentUser() {
    }

    public static AuthPrincipal requireAuthPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthPrincipal principal)) {
            throw new ResponseStatusException(UNAUTHORIZED, "Authentication required");
        }
        return principal;
    }
}
