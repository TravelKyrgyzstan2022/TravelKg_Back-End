package com.example.benomad.security.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RegistrationRequest {

    @NotBlank(message = "Email field can't be empty")
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank(message = "Password field can't be empty")
    private String password;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;
}
