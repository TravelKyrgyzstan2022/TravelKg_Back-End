package com.example.benomad.security.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EmailVerificationRequest {

    @NotBlank(message = "Email field can't be empty")
    @Email(message = "Email is invalid")
    @JsonProperty(required = true)
    private String email;

    @NotBlank
    @JsonProperty(value = "verification_code", required = true)
    private String verificationCode;
}
