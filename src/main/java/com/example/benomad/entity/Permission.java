package com.example.benomad.entity;

public enum Permission {
    ARTICLE_READ("article:read"),
    ARTICLE_WRITE("article:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
