package com.example.benomad.security.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Email field can't be empty")
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank(message = "Password field can't be empty")
    private String password;
}
