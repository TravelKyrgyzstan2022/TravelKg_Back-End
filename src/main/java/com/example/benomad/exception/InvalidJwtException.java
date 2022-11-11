package com.example.benomad.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidJwtException extends CustomException{
    private String message;
    private final Integer statusCode = 400;

    public InvalidJwtException(String message){
        this.message = message;
    }
}
