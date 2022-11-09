package com.example.benomad.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.util.IOUtils;
import com.example.benomad.exception.ContentIsEmptyException;
import com.example.benomad.exception.ContentIsNotImageException;
import com.example.benomad.exception.FailedWhileAccessingImageException;
import com.example.benomad.exception.FailedWhileUploadingException;
import com.example.benomad.mapper.PlaceMapper;
import com.example.benomad.repository.PlaceRepository;
import com.example.benomad.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final AmazonS3 s3;
    @Override
    public boolean saveImageAws(String dataPath,
                                String dataName,
                                Optional<Map<String, String>> optionalMetaData,
                                InputStream inputStream)
            throws AmazonServiceException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        optionalMetaData.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(objectMetadata::addUserMetadata);
            }
        });
        try {
            s3.putObject(dataPath,dataName,inputStream,objectMetadata);
            return true;
        } catch (AmazonServiceException e) {
            throw new FailedWhileUploadingException();
        }
    }

    @Override
    public byte[] getAwsImageByPathAndKey(String path, String key) throws FailedWhileAccessingImageException {
        try {
            return IOUtils.toByteArray(s3.getObject(path, key).getObjectContent());
        }catch (AmazonServiceException | IOException e) {
            throw new FailedWhileAccessingImageException();
        }
    }




    Map<String, String> getMetaData(MultipartFile file) {
        Map<String,String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", ""+ file.getSize());
        return metadata;
    }

    void checkIsImage(MultipartFile file) {
        if(!Arrays.asList(ContentType.IMAGE_GIF.getMimeType(),
                        ContentType.IMAGE_JPEG.getMimeType(),
                        ContentType.IMAGE_PNG.getMimeType(),
                        ContentType.IMAGE_SVG.getMimeType()).
                contains(file.getContentType()))
            throw new ContentIsNotImageException();
    }

    void checkIsNotEmpty(MultipartFile file) {
        if (file.isEmpty()) throw new ContentIsEmptyException();
    }


}
