package com.example.myschedule.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class CustomExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(CourseNameException.class)
    public ResponseEntity<String> handleCourseNameException(CourseNameException e) {
        return buildResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<String> handleCourseNotFoundException(CourseNotFoundException e) {
        return buildResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(GroupAlreadyAssignedException.class)
    public ResponseEntity<String> handleGroupAlreadyAssignedException(GroupAlreadyAssignedException e) {
        return buildResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(GroupNameException.class)
    public ResponseEntity<String> handleGroupNameException(GroupNameException e) {
        return buildResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(GroupNotAssignedException.class)
    public ResponseEntity<String> handleGroupNotAssignedException(GroupNotAssignedException e) {
        return buildResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<String> handleGroupNotFoundExceptionException(GroupNotFoundException e) {
        return buildResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ScheduleTimeException.class)
    public ResponseEntity<String> handleScheduleTimeException(ScheduleTimeException e) {
        return buildResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserAlreadyAssignedException.class)
    public ResponseEntity<String> handleUserAlreadyAssignedException(UserAlreadyAssignedException e) {
        return buildResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserEmailException.class)
    public ResponseEntity<String> handleUserEmailException(UserEmailException e) {
        return buildResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UsernameException.class)
    public ResponseEntity<String> handleUsernameException(UsernameException e) {
        return buildResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return buildResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserRoleException.class)
    public ResponseEntity<String> handleUserRoleException(UserRoleException e) {
        return buildResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserStatusException.class)
    public ResponseEntity<String> handleserStatusException(UserStatusException e) {
        return buildResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<String> buildResponseEntity(String message, HttpStatus status) {
        logger.error("API Request Exception: " + message);
        return ResponseEntity.status(status).body(message);
    }

}
