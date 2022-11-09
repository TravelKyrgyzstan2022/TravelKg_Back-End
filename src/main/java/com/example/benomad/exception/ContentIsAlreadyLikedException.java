package com.example.benomad.exception;

import com.example.benomad.enums.ContentNotFoundEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ContentIsAlreadyLikedException extends CustomException{
    private final String messageFormat = "Couldn't like the %s: the %s has already been liked by the user";
    private final Integer statusCode = 400;
    private String message;

    public ContentIsAlreadyLikedException(ContentNotFoundEnum content){
        this.message = String.format(messageFormat, content.toString(), content.toString());
    }
}