package com.example.universitycms.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RuntimeExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RuntimeExceptionHandler.class);
    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> handleApiRequestExceptionHandler(RuntimeException e) {

        logger.error("API Request Exception: ", e);

        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        Map<String, Object> response = new HashMap<>();
        response.put("message", e.getMessage());
        response.put("httpStatus", badRequest);
        response.put("timeStamp", ZonedDateTime.now(ZoneId.of("Z")));

        return new ResponseEntity<>(response, badRequest);
    }

}
