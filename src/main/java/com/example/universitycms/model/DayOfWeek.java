package com.example.universitycms.model;

public enum DayOfWeek {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday");
    private final String value;

    DayOfWeek(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
