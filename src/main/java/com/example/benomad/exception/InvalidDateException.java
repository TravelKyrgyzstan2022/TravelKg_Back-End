package com.example.benomad.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDateException extends CustomException{
    private final String messageFormat = "Invalid date: %s";
    private String message;
    private final Integer statusCode = 400;

    public InvalidDateException(String message){
        this.message = String.format(messageFormat, message);
    }
}
