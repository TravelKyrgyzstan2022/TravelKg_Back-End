package com.example.benomad.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MessageResponse {
    private String message;
    private Integer status;
    private final LocalDateTime timestamp = LocalDateTime.now();
}
