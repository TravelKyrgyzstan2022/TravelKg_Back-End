package com.example.benomad.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Data
@ResponseStatus(HttpStatus.FORBIDDEN)
public class NoAccessException extends CustomException{
    private final String message = "Has no access to resource";
    private final Integer statusCode = 403;
}
