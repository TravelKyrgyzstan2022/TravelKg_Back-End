package com.example.benomad.security.response;

import com.example.benomad.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("user_info")
    private UserDTO userDTO;
}
