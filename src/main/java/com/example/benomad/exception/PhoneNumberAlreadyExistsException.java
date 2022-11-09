package com.example.benomad.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PhoneNumberAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PhoneNumberAlreadyExistsException(String message){
        super(message);
    }
}