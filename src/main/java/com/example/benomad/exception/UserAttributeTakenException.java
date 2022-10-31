package com.example.benomad.exception;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Data
@ResponseStatus(HttpStatus.CONFLICT)
public class UserAttributeTakenException extends CustomException{
    private String messageFormat = "Attribute %s is invalid. It's already taken by another user";
    private final Integer statusCode = 409;
    private String message;

    public UserAttributeTakenException(String message){
        this.message = String.format(messageFormat, message);
    };
}
