package com.example.benomad.dto;

import com.example.benomad.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(value = {
        "userDTO"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogDTO {
    private Long id;
    private UserDTO authorDTO;
    private Long likes;
    private String title;
    private String body;
    private Status status;

    @JsonProperty("is_liked_by_current_user")
    private Boolean isLikedByCurrentUser;
}
