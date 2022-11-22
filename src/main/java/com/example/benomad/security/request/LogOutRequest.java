package com.example.benomad.security.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LogOutRequest {

    @NotNull(message = "User id can't be null")
    @JsonProperty("user_id")
    private Long userId;
}
