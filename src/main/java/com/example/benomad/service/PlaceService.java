package com.example.benomad.service;

import com.example.benomad.enums.PlaceCategory;
import com.example.benomad.enums.PlaceType;
import com.example.benomad.enums.Region;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.dto.PlaceDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PlaceService {
    List<PlaceDTO> getPlacesByAttributes(String name,
                                         Region region,
                                         PlaceType placeType,
                                         PlaceCategory placeCategory,
                                         String address,
                                         Boolean match,
                                         PageRequest pageRequest,
                                         Long id) throws ContentNotFoundException;
    PlaceDTO getPlaceById(Long id,Long userId) throws ContentNotFoundException;
    Long insertPlace(PlaceDTO placeDTO);
    PlaceDTO deletePlaceById(Long id) throws ContentNotFoundException;
    PlaceDTO updatePlaceById(Long id, PlaceDTO placeDTO) throws ContentNotFoundException;
    PlaceDTO ratePlaceById(Long placeId, Long userId, Integer rating, boolean isRemoval) throws ContentNotFoundException;
    Long insertImageByPlaceId(Long id, MultipartFile file) throws ContentNotFoundException;
    byte[] getImageByPlaceId(Long id);
    Long insertPlaceWithImage(PlaceDTO placeDTO, MultipartFile file);
    List<PlaceDTO> getPlacesByTypesAndCategories(List<PlaceCategory> categories, List<PlaceType> types, Pageable pageable,Long currentUser);
}