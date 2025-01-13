package com.example.myschedule.model;

public enum RoleId {

    ADMIN(1),
    TEACHER(2),
    STUDENT(3),
    STUFF(4);
    private final long value;
    RoleId(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
