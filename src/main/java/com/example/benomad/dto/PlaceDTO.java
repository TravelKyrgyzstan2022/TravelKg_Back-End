package com.example.benomad.dto;


import com.example.benomad.enums.PlaceCategory;
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Schema(required = true)
    private String name;

    @Schema(required = true)
    private Region region;

    @Schema(required = true)
    @JsonProperty("place_type")
    private PlaceType placeType;

    @Schema(required = true)
    @JsonProperty("place_category")
    private PlaceCategory placeCategory;

    @Schema(required = false)
    @JsonProperty(value = "average_rating", access = JsonProperty.Access.READ_ONLY)
    private Double averageRating;

    @Schema(required = false)
    @JsonProperty(value = "rating_count", access = JsonProperty.Access.READ_ONLY)
    private Integer ratingCount;

    @Schema(required = true)
    private String description;

    @Schema(required = true)
    @JsonProperty(value = "image_url", access = JsonProperty.Access.READ_ONLY)
    private String imageUrl;

    @Schema(required = true)
    @JsonProperty("link_url")
    private String linkUrl;

    @Schema(required = false)
    private String address;

    @JsonProperty("is_favorite_of_current_user")
    private boolean isFavoriteOfCurrentUser;
}