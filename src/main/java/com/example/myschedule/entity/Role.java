package com.example.myschedule.entity;

public enum Role {

    STUDENT("Student"),

    TEACHER("Teacher"),

    STUFF("Stuff"),

    ADMIN("Admin");
    private String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }
    public String getRoleName() {
        return roleName;
    }
}
