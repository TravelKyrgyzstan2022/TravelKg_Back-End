
package com.example.benomad.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties("favorite_places")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String login;

    private String password;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String email;

    @JsonProperty(value = "favorite_places",access = JsonProperty.Access.READ_ONLY)
    private List<PlaceDTO> placeDTOS;


}
