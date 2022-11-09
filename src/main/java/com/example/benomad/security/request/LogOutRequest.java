package com.example.benomad.security.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LogOutRequest {

    @NotNull(message = "User id can't be null")
    private Long userId;
}
