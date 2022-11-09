package com.example.benomad.dto;


import com.example.benomad.enums.PlaceType;
import com.example.benomad.enums.Region;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceDTO {
    private Long id;

    private String name;

    private Region region;

    @JsonProperty("place_type")
    private PlaceType placeType;

    @JsonProperty("average_rating")
    private Double averageRating;

    @JsonProperty("rating_count")
    private Integer ratingCount;

    private String description;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("link_url")
    private String linkUrl;

    private String address;
}