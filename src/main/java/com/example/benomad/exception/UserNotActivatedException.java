package com.example.benomad.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Data
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserNotActivatedException extends CustomException{
    private final String message = "Account is not activated";
    private final Integer statusCode = 403;
}
