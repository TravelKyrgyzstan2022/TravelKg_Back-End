package com.example.benomad.dto;

import com.fasterxml.jackson.annotation.JsonInclude;;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(value = "user_id",access = JsonProperty.Access.READ_ONLY)
    private Long userId;

    @NotBlank(message = "Body can't be null or empty")
    private String body;

    @NotBlank(message = "Title can't be null or empty")
    private String title;

    @JsonProperty(value = "image_urls",access = JsonProperty.Access.READ_ONLY)
    private List<String> imageUrls;
}
