package com.gucardev.tapucase.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CodeAlreadyExistsException extends RuntimeException{
    public CodeAlreadyExistsException(String message){
        super(message);
    }
}
