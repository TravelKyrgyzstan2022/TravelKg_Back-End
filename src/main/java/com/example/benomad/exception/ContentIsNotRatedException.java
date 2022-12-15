package com.example.benomad.exception;

import com.example.benomad.enums.Content;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Data
@ResponseStatus(HttpStatus.CONFLICT)
public class ContentIsNotRatedException extends CustomException{
    private final String messageFormat = "Couldn't remove rating of the %s: the %s wasn't rated by the user before";
    private final Integer statusCode = 409;
    private String message;

    public ContentIsNotRatedException(Content content){
        this.message = String.format(messageFormat, content.toString(), content.toString());
    }
}