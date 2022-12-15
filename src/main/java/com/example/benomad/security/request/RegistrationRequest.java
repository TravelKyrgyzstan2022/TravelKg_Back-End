package com.example.benomad.security.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RegistrationRequest {

    @NotBlank(message = "Email can't be null or empty")
    @Email(message = "Email is invalid")
    @JsonProperty(required = true)
    private String email;

    @NotBlank(message = "Password can't be null or empty")
    @JsonProperty(required = true)
    private String password;

    @NotBlank(message = "First name can't be null or empty")
    @JsonProperty(value = "first_name", required = true)
    private String firstName;

    @NotBlank(message = "Last name can't be null or empty")
    @JsonProperty(value = "last_name", required = true)
    private String lastName;
}
