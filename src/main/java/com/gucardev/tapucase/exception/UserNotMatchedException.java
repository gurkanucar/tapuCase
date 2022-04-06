package com.gucardev.tapucase.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotMatchedException extends RuntimeException {
    public UserNotMatchedException(String message){
        super(message);
    }

}