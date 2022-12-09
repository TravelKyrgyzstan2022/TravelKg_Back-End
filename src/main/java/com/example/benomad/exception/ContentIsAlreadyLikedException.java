package com.example.benomad.exception;

import com.example.benomad.enums.Content;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ResponseStatus(HttpStatus.CONFLICT)
public class ContentIsAlreadyLikedException extends CustomException{
    private final String messageFormat = "Couldn't like the %s: the %s has already been liked by the user";
    private final Integer statusCode = 409;
    private String message;

    public ContentIsAlreadyLikedException(Content content){
        this.message = String.format(messageFormat, content.toString(), content.toString());
    }
}