package com.example.myschedule.exception;

public class ScheduleTimeException extends RuntimeException{

    public ScheduleTimeException() {

    }

    public ScheduleTimeException(String message) {
        super(message);
    }
}
