package com.example.benomad.dto;


import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PlaceDTO {
    private Long id;
    private String name;
    private RegionDTO regionDTO;
    private PlaceTypeDTO placeTypeDTO;
    private String description;
    private String imageUrl;
    private String linkUrl;
    private String address;
}
