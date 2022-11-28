package com.example.benomad.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Data
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidVerificationCodeException extends CustomException{
    private String message;
    private final String messageFormat = "Invalid verification code: %s";
    private final Integer statusCode = 460;

    public InvalidVerificationCodeException(String message){
        this.message = String.format(messageFormat, message);
    }
}
