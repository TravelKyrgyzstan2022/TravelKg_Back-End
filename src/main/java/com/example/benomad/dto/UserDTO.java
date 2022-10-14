
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
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private List<PlaceDTO> placeDTOS;
}

