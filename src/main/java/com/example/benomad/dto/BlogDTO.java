package com.example.benomad.dto;

import com.example.benomad.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogDTO {
    private Long id;

    @JsonProperty("author_id")
    private Long authorId;

    private Long likes;

    private String title;

    private String body;

    private Status status;

    @JsonProperty("is_liked_by_current_user")
    private Boolean isLikedByCurrentUser;
}
