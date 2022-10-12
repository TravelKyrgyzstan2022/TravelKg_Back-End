package com.example.benomad.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(value = {
        "id",
        "user"
})
public class ArticleDTO {
    private Long id;
    private UserDTO userDTO;
    private String body;
    private String title;
    private String imageUrl;
}
