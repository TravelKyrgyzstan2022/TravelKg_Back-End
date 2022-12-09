package com.example.benomad.security.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class CodeRequest {

    @NotBlank(message = "Email field can't be empty")
    @Email(message = "Email is invalid")
    @JsonProperty(required = true)
    private String email;

}
