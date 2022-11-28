package com.example.benomad.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserNotActivatedException extends CustomException{
    private final String message = "Account is not activated";
    private final Integer statusCode = 403;
}
