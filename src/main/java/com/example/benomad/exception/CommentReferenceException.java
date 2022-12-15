package com.example.benomad.exception;

import com.example.benomad.enums.CommentReference;
import com.example.benomad.enums.Content;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Data
@ResponseStatus(HttpStatus.CONFLICT)
public class CommentReferenceException extends CustomException{
    private final String messageFormat = "Comment Reference error: The comment doesn't belong to given %s";
    private final Integer statusCode = 409;
    private String message;

    public CommentReferenceException(CommentReference reference){
        this.message = String.format(messageFormat, reference.toString());
    }
}
