package com.example.benomad.dto;


import com.example.benomad.enums.PlaceType;
import com.example.benomad.enums.Region;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PlaceDTO {
    private Long id;
    private String name;
    private Region region;
    private PlaceType placeType;
    private String description;
    private String imageUrl;
    private String linkUrl;
    private String address;
}
