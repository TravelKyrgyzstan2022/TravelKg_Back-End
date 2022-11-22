package com.example.benomad.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRatingException extends CustomException{
    private final String message = "Rating value is out of bounds (1 - 5)";
    private final Integer statusCode = 400;
}
