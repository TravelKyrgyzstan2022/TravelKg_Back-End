package com.example.benomad.dto;

import com.fasterxml.jackson.annotation.JsonInclude;;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    private String body;

    private String title;

    @JsonProperty(value = "image_url",access = JsonProperty.Access.READ_ONLY)
    private String imageUrl;
}
