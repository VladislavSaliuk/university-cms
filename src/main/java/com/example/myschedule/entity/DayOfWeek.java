package com.example.myschedule.entity;

public enum DayOfWeek {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday");
    private String dayOfWeekName;
    DayOfWeek(String dayOfWeekName) {
        this.dayOfWeekName = dayOfWeekName;
    }
    public String getDayOfWeekName() {
        return dayOfWeekName;
    }
}