package com.example.universitycms.model;

public enum RoleValue {

    ADMIN(1),
    TEACHER(2),
    STUDENT(3),
    STUFF(4);
    private final long roleId;
    RoleValue(long roleId) {
        this.roleId = roleId;
    }

    public long getRoleId() {
        return roleId;
    }
}
