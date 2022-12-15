package com.example.benomad.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FavoritePlaceException extends CustomException{
    private String message;
    private final Integer statusCode = 400;

    public FavoritePlaceException(boolean isFavorite){
        if(isFavorite){
            this.message = "Can't add place to favorites: Place is already in user's favorites";
        }else{
            this.message = "Can't remove place from favorites: Place is not in user's favorites";
        }
    }
}
