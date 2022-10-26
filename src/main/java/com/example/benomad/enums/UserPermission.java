package com.example.benomad.enums;



public enum UserPermission {
    CONTENT_READ("content:read"),
    CONTENT_MODIFY("content:modify"),
    USER_MODIFY("user:modify");

    private final String permissionString;

    UserPermission(String permissionString) {
        this.permissionString = permissionString;
    }

    public String getPermissionString() {
        return permissionString;
    }


}

