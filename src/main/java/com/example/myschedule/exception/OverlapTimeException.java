package com.example.myschedule.exception;

public class OverlapTimeException extends RuntimeException {
    public OverlapTimeException(String message) {
        super(message);
    }

}