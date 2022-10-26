package com.example.benomad.enums;



import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.benomad.enums.UserPermission.*;

public enum UserRole {
    MODERATOR(Sets.newHashSet(USER_MODIFY,CONTENT_READ,CONTENT_MODIFY)),
    ACTIVE(Sets.newHashSet(CONTENT_READ,CONTENT_MODIFY)),
    BANNED(Sets.newHashSet(CONTENT_READ));

    private Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities(){
        Set<SimpleGrantedAuthority> authorities = getPermissions().stream().map(x -> new SimpleGrantedAuthority(x.getPermissionString())).collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this));
        return authorities;
    }
}
