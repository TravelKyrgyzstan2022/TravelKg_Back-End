package com.example.benomad.service;

import com.example.benomad.dto.ImageDTO;
import com.example.benomad.enums.ImagePath;
import com.example.benomad.exception.ContentIsEmptyException;
import com.example.benomad.exception.ContentIsNotImageException;
import com.example.benomad.exception.ContentNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;

public interface ImageService {
    List<String> uploadImages(MultipartFile[] files, ImagePath path);
    void checkImage(MultipartFile file);
    String getRandomUUID();

    List<String> uploadImages64(ImageDTO [] files,ImagePath path);
    String uploadImage64(ImageDTO  file,ImagePath path);
}
