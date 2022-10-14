package com.example.travel.dto;


import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegionDTO {
    private Long id;
    private String name;
}
