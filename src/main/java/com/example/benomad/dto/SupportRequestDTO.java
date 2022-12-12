package com.example.benomad.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SupportRequestDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(value = "user", access = JsonProperty.Access.READ_ONLY)
    private UserDTO user;

    @JsonProperty(value = "datetime", access = JsonProperty.Access.READ_ONLY)
    private String dateTime;
}
