package com.example.benomad.dto;


import com.example.benomad.enums.PlaceType;
import com.example.benomad.enums.Region;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceDTO {
    private Long id;

    @Schema(required = true)
    private String name;

    @Schema(required = true)
    private Region region;

    @Schema(required = true)
    @JsonProperty("place_type")
    private PlaceType placeType;

    @Schema(required = false)
    @JsonProperty("average_rating")
    private Double averageRating;

    @Schema(required = false)
    @JsonProperty("rating_count")
    private Integer ratingCount;

    @Schema(required = true)
    private String description;

    @Schema(required = true)
    @JsonProperty("image_url")
    private String imageUrl;

    @Schema(required = true)
    @JsonProperty("link_url")
    private String linkUrl;

    @Schema(required = false)
    private String address;
}