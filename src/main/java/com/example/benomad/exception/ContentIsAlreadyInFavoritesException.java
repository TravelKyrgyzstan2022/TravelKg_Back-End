package com.example.benomad.exception;

import com.example.benomad.enums.ContentNotFoundEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ResponseStatus(HttpStatus.CONFLICT)
public class ContentIsAlreadyInFavoritesException extends CustomException{
    private final String messageFormat = "Couldn't add to favorites the %s: the %s has already been added to favorites by the user";
    private final Integer statusCode = 409;
    private String message;

    public ContentIsAlreadyInFavoritesException(ContentNotFoundEnum content){
        this.message = String.format(messageFormat, content.toString(), content.toString());
    }
}