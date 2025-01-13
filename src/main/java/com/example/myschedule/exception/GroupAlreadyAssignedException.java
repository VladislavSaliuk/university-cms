package com.example.myschedule.exception;

public class GroupAlreadyAssignedException extends RuntimeException{

    public GroupAlreadyAssignedException() {
    }


    public GroupAlreadyAssignedException(String message) {
        super(message);
    }


}
