package com.example.benomad.security.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class JwtResponse {

    private final String type = "Bearer";

    private String token;

    private String refreshToken;

    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String email;


    private List<String> roles;
}
