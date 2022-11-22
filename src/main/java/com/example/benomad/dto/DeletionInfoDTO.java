package com.example.benomad.dto;


import com.example.benomad.entity.User;
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

    private LocalDate deletionDate;

    private Long responsibleUserId;
}
