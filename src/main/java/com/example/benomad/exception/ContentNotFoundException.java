package com.example.benomad.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ContentNotFoundException extends Exception{
    private final String message = "Content not found.";
}
