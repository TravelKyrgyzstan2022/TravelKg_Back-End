package com.example.benomad.dto;

import com.example.benomad.entity.DeletionInfo;
import com.example.benomad.enums.ReviewStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty("author_id")
    private Long authorId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long likes;

    private String title;

    private String body;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @JsonProperty("is_deleted")
    private boolean isDeleted;

    @JsonProperty("deletion_info")
    private DeletionInfoDTO deletionInfoDTO;

    private ReviewStatus reviewStatus;

    @JsonProperty(value = "is_liked_by_current_user", access = JsonProperty.Access.READ_ONLY)
    private Boolean isLikedByCurrentUser;
}
