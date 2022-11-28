package com.example.benomad.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyActivatedException extends CustomException{
    private final String message = "User is already activated!";
    private final Integer statusCode = 409;
}
