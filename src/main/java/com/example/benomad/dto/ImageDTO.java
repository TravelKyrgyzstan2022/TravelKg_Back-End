package com.example.benomad.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDTO {
    @NotBlank(message = "Url can't be null or empty")
    private String imageUrl;
}
