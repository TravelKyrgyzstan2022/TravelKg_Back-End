package com.example.benomad.security.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class TokenRefreshRequest {
    @NotBlank(message = "Refresh token can't be null or empty")
    @JsonProperty(value = "refreshtoken", required = true)
    private String refreshToken;
}
