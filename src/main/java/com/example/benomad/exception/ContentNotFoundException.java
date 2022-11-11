package com.example.benomad.exception;

import com.example.benomad.enums.ContentNotFoundEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Data
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ContentNotFoundException extends CustomException{
    private final String messageFormat = "%s with %s : {%s} was not found";
    private final Integer statusCode = 404;
    private String message;

    public ContentNotFoundException(ContentNotFoundEnum content, String attribute, String value){
        this.message = String.format(messageFormat, content.toString(), attribute, value);
    }
}
