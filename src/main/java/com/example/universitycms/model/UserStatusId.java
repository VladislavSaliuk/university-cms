package com.example.universitycms.model;

public enum UserStatusId {

    ACTIVE(1),
    BANNED(2);
    private final long value;

    UserStatusId(long value) {
        this.value = value;
    }
    public long getValue() {
        return value;
    }
}
