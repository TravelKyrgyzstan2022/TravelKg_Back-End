package com.example.benomad.dto;


import com.example.benomad.enums.PlaceCategory;
import com.example.benomad.enums.PlaceType;
import com.example.benomad.enums.Region;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Name can't be null or empty")
    @Schema(required = true)
    private String name;

    @NotBlank(message = "Region can't be null or empty")
    @Schema(required = true)
    private Region region;

    @NotBlank(message = "Type can't be null or empty")
    @Schema(required = true)
    @JsonProperty("place_type")
    private PlaceType placeType;

    @NotBlank(message = "Category can't be null or empty")
    @Schema(required = true)
    @JsonProperty("place_category")
    private PlaceCategory placeCategory;

    @NotBlank(message = "Description can't be null or empty")
    @Schema(required = true)
    private String description;

    @Schema(required = false)
    @JsonProperty(value = "average_rating", access = JsonProperty.Access.READ_ONLY)
    private Double averageRating;

    @Schema(required = false)
    @JsonProperty(value = "rating_count", access = JsonProperty.Access.READ_ONLY)
    private Integer ratingCount;

    @JsonProperty(value = "comment_count", access = JsonProperty.Access.READ_ONLY)
    private Integer commentCount;

    @Schema(required = true)
    @JsonProperty(value = "image_urls",access = JsonProperty.Access.READ_ONLY)
    private List<String> imageUrls;

    @NotBlank(message = "Link can't be null or empty")
    @Schema(required = true)
    @JsonProperty("link_url")
    private String linkUrl;

    @NotBlank(message = "Address can't be null or empty")
    @Schema(required = false)
    private String address;

    @JsonProperty(value = "is_favorite_of_current_user", access = JsonProperty.Access.READ_ONLY)
    private Boolean isFavoriteOfCurrentUser;

    @NotBlank(message = "Latitude can't be null or empty")
    @Schema(required = true)
    @JsonProperty(value = "latitude")
    private double latitude;

    @NotBlank(message = "Longtitude can't be null or empty")
    @Schema(required = true)
    @JsonProperty(value = "longitude")
    private double longitude;
}
