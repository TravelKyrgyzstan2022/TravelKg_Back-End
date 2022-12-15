package com.example.benomad.dto;




import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;

    @JsonProperty(value = "user", access = JsonProperty.Access.READ_ONLY)
    private UserDTO user;

    @NotBlank(message = "Body can't be null or empty")
    private String body;

    @JsonProperty(value = "creation_date", access = JsonProperty.Access.READ_ONLY)
    private String creationDate;

    @JsonProperty(value = "update_date", access = JsonProperty.Access.READ_ONLY)
    private String updateDate;

    @JsonProperty(value = "like_count", access = JsonProperty.Access.READ_ONLY)
    private Integer likeCount;

    @JsonProperty(value = "is_liked_by_current_user", access = JsonProperty.Access.READ_ONLY)
    private Boolean isLikedByCurrentUser;

    @JsonProperty(value = "deletion_info", access = JsonProperty.Access.READ_ONLY)
    private DeletionInfoDTO deletionInfo;
}
