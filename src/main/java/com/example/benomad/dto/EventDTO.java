package com.example.benomad.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Schema(required = true)
    private String name;

    @JsonProperty(value = "datetime", required = true)
    private String dateTime;

    @Schema(required = true)
    private String description;

    @Schema(required = true)
    @JsonProperty(value = "image_urls",access = JsonProperty.Access.READ_ONLY)
    private List<String> imageUrls;

    @Schema(required = true)
    @JsonProperty("link_url")
    private String linkUrl;

    @Schema(required = false)
    private String address;
}
