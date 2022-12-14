package com.example.benomad.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DeletionInfoDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Reason can't be null or empty")
    private String reason;

    @JsonProperty(value = "deletion_date", access = JsonProperty.Access.READ_ONLY)
    private LocalDate deletionDate;

    @JsonProperty(value = "responsible_user_id", access = JsonProperty.Access.READ_ONLY)
    private Long responsibleUserId;
}
