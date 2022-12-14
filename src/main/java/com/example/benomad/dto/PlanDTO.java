package com.example.benomad.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Date can't be empty or null")
    @JsonProperty("date")
    private String date;

    @JsonProperty(value = "user", access = JsonProperty.Access.READ_ONLY)
    private UserDTO user;

    @JsonProperty(value = "place", access = JsonProperty.Access.READ_ONLY)
    private PlaceDTO place;

    @NotBlank(message = "Place ID can't be empty or null")
    @JsonProperty(value = "place_id", access = JsonProperty.Access.WRITE_ONLY)
    private Long placeId;

    @NotBlank(message = "Note can't be empty or null")
    private String note;
}
