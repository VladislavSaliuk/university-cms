package com.example.universitycms.exceptions;

public class UserStatusException extends org.springframework.security.core.AuthenticationException{
    public UserStatusException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserStatusException(String msg) {
        super(msg);
    }
}
