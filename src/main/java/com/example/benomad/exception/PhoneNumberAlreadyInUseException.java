package com.example.benomad.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PhoneNumberAlreadyInUseException extends RuntimeException {
    private final String message = "Phone number is already in use!";
    private final Integer statusCode = 400;
}