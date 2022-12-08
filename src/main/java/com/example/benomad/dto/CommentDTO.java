package com.example.benomad.dto;



import com.example.benomad.enums.CommentReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;

    @JsonProperty("reference_id")
    private Long referenceId;

    @JsonProperty("user")
    private UserDTO user;

    private String body;

    @JsonProperty(value = "creation_date", access = JsonProperty.Access.READ_ONLY)
    private String creationDate;

    @JsonProperty(value = "update_date", access = JsonProperty.Access.READ_ONLY)
    private String updateDate;

    @JsonProperty(value = "like_count", access = JsonProperty.Access.READ_ONLY)
    private Long likeCount;

    @JsonProperty(value = "is_liked_by_current_user", access = JsonProperty.Access.READ_ONLY)
    private Boolean isLikedByCurrentUser;

    @JsonProperty(value = "deletion_info", access = JsonProperty.Access.READ_ONLY)
    private DeletionInfoDTO deletionInfo;

    private CommentReference reference;
}
