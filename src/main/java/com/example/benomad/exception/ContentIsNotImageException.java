package com.example.benomad.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@NoArgsConstructor
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ContentIsNotImageException  extends CustomException{
    private final String messageFormat = "Content type %s is not appropriate";
    private final Integer statusCode = 400;
    private String message;

    public ContentIsNotImageException(String contentType){
        this.message = String.format(messageFormat, contentType);
    }
}
