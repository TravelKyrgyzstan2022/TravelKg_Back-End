package com.example.benomad.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DeletionInfoDTO {

    private Long id;

    private String reason;

    @JsonProperty("deletion_date")
    private LocalDate deletionDate;

    @JsonProperty("responsible_user_id")
    private Long responsibleUserId;
}
