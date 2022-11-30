package com.example.benomad.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GetPlanRequest {
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("date")
    private LocalDate date;

}
