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
    List<PlaceDTO> getPlacesByAttributes(String name, Region region, PlaceType placeType, String address,Boolean match,PageRequest pageRequest) throws ContentNotFoundException;
    PlaceDTO getPlaceById(Long id) throws ContentNotFoundException;
    PlaceDTO insertPlace(PlaceDTO placeDTO);
    PlaceDTO deletePlaceById(Long id) throws ContentNotFoundException;
    PlaceDTO updatePlaceById(Long id, PlaceDTO placeDTO) throws ContentNotFoundException;
    PlaceDTO ratePlaceById(Long placeId, Integer rating, boolean isRemoval) throws ContentNotFoundException;
    List<PlaceDTO> getPlacesByTypesAndCategories(List<PlaceCategory> categories, List<PlaceType> types, Pageable pageable);
    Long insertImageByPlaceId(Long id, MultipartFile file) throws ContentNotFoundException;
    byte[] getImageByPlaceId(Long id);

    PlaceDTO addPlaceToFavorites(Long id) throws ContentNotFoundException;
}