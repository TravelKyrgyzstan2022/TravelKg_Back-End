package com.example.benomad.security.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_SUPERADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
