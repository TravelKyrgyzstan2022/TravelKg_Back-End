package com.example.benomad.service.impl;

import com.example.benomad.entity.ImageData;
import com.example.benomad.exception.ImageNotFoundException;
import com.example.benomad.repository.ImageDataRepository;
import com.example.benomad.util.ImageUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Data
public class ImageDataService {


    private final ImageDataRepository imageDataRepository;

    public Long uploadImage(MultipartFile file) {


        try {
            return imageDataRepository.save(ImageData.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .imageData(ImageUtil.compressImage(file.getBytes())).build()).getId();
        } catch (IOException e) {
            return null;
        }

    }

    @Transactional
    public byte[] getImage(Long id) {
        Optional<ImageData> dbImage = imageDataRepository.findById(id);
        return  ImageUtil.decompressImage(dbImage.orElseThrow(ImageNotFoundException::new).getImageData());
    }


}
