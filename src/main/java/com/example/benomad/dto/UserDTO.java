package com.example.benomad.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(value = {
        "login",
        "password"
})
public class UserDTO {
    private Long id;
    private String login;
    private String password;
}
