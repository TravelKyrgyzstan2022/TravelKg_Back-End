package com.example.benomad.exception;

import com.example.benomad.enums.ContentNotFoundEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ContentIsAlreadyInFavoritesException extends CustomException{
    private final String messageFormat = "Couldn't add the %s: the %s has already been added to user's favorites";
    private final Integer statusCode = 400;
    private String message;

    public ContentIsAlreadyInFavoritesException(ContentNotFoundEnum content){
        this.message = String.format(messageFormat, content, content);
    }
}
