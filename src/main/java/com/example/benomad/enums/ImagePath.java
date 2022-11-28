package com.example.benomad.enums;

import lombok.Getter;

@Getter
public enum ImagePath {
    BLOG("blogs"),
    ARTICLE("articles"),
    PLACE("places"),
    USER("users");

    private final String pathToImage;
    ImagePath(String pathToImage) {
        this.pathToImage = pathToImage;
    }
}
