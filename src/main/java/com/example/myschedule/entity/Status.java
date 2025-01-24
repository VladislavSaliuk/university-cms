package com.example.myschedule.entity;

public enum Status {
    ACTIVE("Active"),
    BANNED("Banned");
    private String statusName;
    Status(String statusName) {
        this.statusName = statusName;
    }
    public String getStatusName() {
        return statusName;
    }
}
