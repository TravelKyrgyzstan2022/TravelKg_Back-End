package com.example.benomad.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanDTO {

    private Long id;

    @JsonProperty("date")
    private LocalDate date;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("place_id")
    private Long placeId;

    private String note;
}
