package com.example.benomad.dto;



import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    Long id;

    @JsonProperty("place_id")
    private Long placeId;

    @JsonProperty("user_id")
    private Long userId;

    private String body;
}
