package com.example.benomad.service;

import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.exception.FailedWhileAccessingImageException;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

public interface ImageService {
    byte[] getAwsImageByPathAndKey(String path, String key)  throws FailedWhileAccessingImageException;
    void saveImageAws(String dataPath,
                         String dataName,
                         Optional<Map<String, String>> optionalMetaData,
                         InputStream inputStream) throws ContentNotFoundException;
}
