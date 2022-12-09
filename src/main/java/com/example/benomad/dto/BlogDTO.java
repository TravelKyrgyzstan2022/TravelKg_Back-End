package com.example.benomad.dto;

import com.example.benomad.enums.ReviewStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(value = "author", access = JsonProperty.Access.READ_ONLY)
    private UserDTO author;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long likes;

    private String title;

    private String body;

    @Schema(required = true)
    @JsonProperty(value = "image_urls",access = JsonProperty.Access.READ_ONLY)
    private List<String> imageUrls;

    @JsonProperty(value = "creation_date", access = JsonProperty.Access.READ_ONLY)
    private LocalDate creationDate;

    @JsonProperty(value = "update_date", access = JsonProperty.Access.READ_ONLY)
    private LocalDate updateDate;

    @JsonProperty(value = "is_deleted", access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    @JsonProperty(value = "deletion_info", access = JsonProperty.Access.READ_ONLY)
    private DeletionInfoDTO deletionInfoDTO;

    @JsonProperty(value = "review_status", access = JsonProperty.Access.READ_ONLY)
    private ReviewStatus reviewStatus;

    @JsonProperty(value = "is_liked_by_current_user", access = JsonProperty.Access.READ_ONLY)
    private Boolean isLikedByCurrentUser;

    @Schema(required = true)
    @JsonProperty(value = "image_url", access = JsonProperty.Access.READ_ONLY)
    private String imageUrl;
}
