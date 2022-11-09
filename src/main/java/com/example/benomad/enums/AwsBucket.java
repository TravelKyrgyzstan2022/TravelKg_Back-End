package com.example.benomad.enums;

import lombok.Getter;

@Getter
public enum AwsBucket {
    MAIN_BUCKET("travelbucket11");

    private final String bucketName;
    AwsBucket(String bucketName) {
        this.bucketName = bucketName;
    }
}
