package com.example.benomad.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Getter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FailedWhileAccessingImageException extends CustomException{
    private final String messageFormat = "Something went wrong while accessing image";
    private final Integer statusCode = 400;
    private String message;

    public FailedWhileAccessingImageException(String message) {
        this.message = message;
    }
}
