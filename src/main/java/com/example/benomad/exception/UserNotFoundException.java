package com.example.benomad.exception;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Data
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends CustomException{
    private final String message = "User not found";
    private final Integer statusCode = 404;
}
