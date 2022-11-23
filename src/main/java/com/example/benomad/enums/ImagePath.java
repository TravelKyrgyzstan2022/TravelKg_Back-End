package com.example.benomad.enums;

import lombok.Getter;

@Getter
public enum ImagePath {
    BLOG("blogs"),
    ARTICLE("articles"),
    PLACE("places");

    private final String pathToImage;
    ImagePath(String pathToImage) {
        this.pathToImage = pathToImage;
    }
}
