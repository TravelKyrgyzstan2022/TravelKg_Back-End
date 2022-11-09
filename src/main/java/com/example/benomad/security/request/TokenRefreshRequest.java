package com.example.benomad.security.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class TokenRefreshRequest {
    @NotBlank(message = "Refresh token can't be empty")
    private String refreshToken;
}
