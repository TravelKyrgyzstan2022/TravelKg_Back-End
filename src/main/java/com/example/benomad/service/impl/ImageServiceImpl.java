package com.example.benomad.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.benomad.dto.ImageDTO;
import com.example.benomad.enums.ImagePath;
import com.example.benomad.exception.ContentIsEmptyException;
import com.example.benomad.exception.ContentIsNotImageException;
import com.example.benomad.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final String cloudinaryUrl = "cloudinary://619757622473734:1ZQqo0b7Il7jP6K_RAOPIAzX0so@benomad";
    private final Cloudinary cloudinary = new Cloudinary((cloudinaryUrl));


    @SneakyThrows
    public List<String> uploadImages(MultipartFile[] files, ImagePath path) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            checkImage(file);
            var upload = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder",path.getPathToImage(),
                    "public_id", getRandomUUID(),
                    "unique_filename", "true"
            ));
            urls.add((String) upload.get("secure_url"));
        }
        return urls;
    }

    @SneakyThrows
    public String uploadImage(MultipartFile file, ImagePath path) {
        checkImage(file);
        return (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "folder",path.getPathToImage(),
                "public_id", getRandomUUID(),
                "unique_filename", "true"
        )).get("secure_url");
    }




    public void checkImage(MultipartFile file) {
        if (!Arrays.asList(ContentType.IMAGE_GIF.getMimeType(),
                        ContentType.IMAGE_JPEG.getMimeType(),
                        ContentType.IMAGE_PNG.getMimeType(),
                        ContentType.IMAGE_SVG.getMimeType()).
                contains(file.getContentType()))
            throw new ContentIsNotImageException(file.getContentType());
        if (file.isEmpty()) throw new ContentIsEmptyException(file.getOriginalFilename());
    }

    public String getRandomUUID() {
        String uniqueUUID =""+ UUID.randomUUID() + UUID.randomUUID() + UUID.randomUUID();
        return uniqueUUID.replaceAll("[^a-zA-z0-9]","");
    }

    @SneakyThrows
    @Override
    public List<String> uploadImages64(ImageDTO [] files,ImagePath path) {
        List<String> imageUrls = new ArrayList<>();
        for (ImageDTO file : files) {
            imageUrls.add((String) cloudinary.uploader().upload(file.getImageUrl(), ObjectUtils.asMap(
                    "folder",path.getPathToImage(),
                    "public_id", getRandomUUID(),
                    "unique_filename", "true"
            )).get("secure_url"));
        }
        return imageUrls;
    }

    @SneakyThrows
    @Override
    public String uploadImage64(ImageDTO file,ImagePath path) {
        return (String) cloudinary.uploader().upload(file.getImageUrl(), ObjectUtils.asMap(
                "folder",path.getPathToImage(),
                "public_id", getRandomUUID(),
                "unique_filename", "true"
        )).get("secure_url");
    }

}