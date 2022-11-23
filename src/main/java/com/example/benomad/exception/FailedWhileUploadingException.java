package com.example.benomad.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Getter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FailedWhileUploadingException extends CustomException{
    private final String messageFormat = "Something went wrong while uploading %s";
    private final Integer statusCode = 400;
    private String message;
    public FailedWhileUploadingException(String fileName) {
        this.message = String.format(messageFormat,fileName);
    }
}
