package com.example.benomad.exception;

import com.example.benomad.enums.ContentNotFoundEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Data
@ResponseStatus(HttpStatus.CONFLICT)
public class ContentIsNotLikedException extends CustomException{
    private final String messageFormat = "Couldn't dislike the %s: the %s wasn't liked by the user before";
    private final Integer statusCode = 409;
    private String message;

    public ContentIsNotLikedException(ContentNotFoundEnum content){
        this.message = String.format(messageFormat, content.toString(), content.toString());
    }
}