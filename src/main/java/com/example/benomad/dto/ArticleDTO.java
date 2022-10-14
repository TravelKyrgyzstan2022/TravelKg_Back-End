package com.example.benomad.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(value = {
        "userDTO"
})
@JsonIncludeProperties(value = "")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {
    private Long id;
    private UserDTO userDTO;
    private String body;
    private String title;
    private String imageUrl;
}
