package com.example.benomad.security.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Email field can't be null or empty")
    @Email(message = "Email is invalid")
    @JsonProperty(required = true)
    private String email;

    @NotBlank(message = "Password field can't be null or empty")
    @JsonProperty(required = true)
    private String password;
}
