package com.example.benomad.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(value = {
        "password"
})
public class UserDTO {
    private Long id;
    private String login;
    private String password;
    private List<PlaceDTO> placeDTOS;
}
