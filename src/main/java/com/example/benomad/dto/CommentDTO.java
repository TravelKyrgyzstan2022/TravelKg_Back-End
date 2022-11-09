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

    @JsonProperty("user_id")
    private Long userId;

    private String body;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String creationDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long likeCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isLikedByCurrentUser;

    private CommentReference reference;
}
