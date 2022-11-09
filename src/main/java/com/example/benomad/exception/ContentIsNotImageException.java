package com.example.benomad.exception;

import com.example.benomad.enums.ContentNotFoundEnum;
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

    public ContentIsNotImageException(ContentNotFoundEnum content){
        this.message = String.format(messageFormat, content);
    }
}
