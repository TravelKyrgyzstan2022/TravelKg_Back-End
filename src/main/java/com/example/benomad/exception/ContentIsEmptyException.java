package com.example.benomad.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ContentIsEmptyException extends CustomException{
    private final String messageFormat = "Image %s could not be empty";
    private final Integer statusCode = 400;
    private String message;

    public ContentIsEmptyException(String message) {
        this.message = String.format(messageFormat,message);
    }
}
