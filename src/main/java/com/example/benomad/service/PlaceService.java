package com.example.benomad.service;

import com.example.benomad.enums.PlaceType;
import com.example.benomad.enums.Region;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.dto.PlaceDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PlaceService {
    List<PlaceDTO> getPlacesByAttributes(String name, Region region, PlaceType placeType, String address,Boolean match,PageRequest pageRequest) throws ContentNotFoundException;
    PlaceDTO getPlaceById(Long id) throws ContentNotFoundException;
    Long insertPlace(PlaceDTO placeDTO);
    PlaceDTO deletePlaceById(Long id) throws ContentNotFoundException;
    PlaceDTO updatePlaceById(Long id, PlaceDTO placeDTO) throws ContentNotFoundException;
    PlaceDTO ratePlaceById(Long placeId, Long userId, Integer rating, boolean isRemoval) throws ContentNotFoundException;
    PlaceDTO insertImageByPlaceId(Long id, MultipartFile file) throws ContentNotFoundException;
    byte[] getImageByPlaceId(Long id);
}