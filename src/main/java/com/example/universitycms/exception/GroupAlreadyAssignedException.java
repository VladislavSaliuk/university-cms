package com.example.universitycms.exception;

public class GroupAlreadyAssignedException extends RuntimeException{

    public GroupAlreadyAssignedException() {
    }


    public GroupAlreadyAssignedException(String message) {
        super(message);
    }


}
