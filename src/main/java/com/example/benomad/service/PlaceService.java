package com.example.benomad.service;

import com.example.benomad.enums.PlaceCategory;
import com.example.benomad.enums.PlaceType;
import com.example.benomad.enums.Region;
import com.example.benomad.exception.PlaceNotFoundException;
import com.example.benomad.dto.PlaceDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PlaceService {
    List<PlaceDTO> getPlacesByAttributes(String name, Region region, PlaceType placeType, PlaceCategory placeCategory, String address, Boolean match, Long currentUser,PageRequest pageRequest) throws PlaceNotFoundException;
    PlaceDTO getPlaceById(Long id,Long currentUser) throws PlaceNotFoundException;
    PlaceDTO insertPlace(PlaceDTO placeDTO, MultipartFile file);
    PlaceDTO deletePlaceById(Long id) throws PlaceNotFoundException;
    PlaceDTO updatePlaceById(Long id, PlaceDTO placeDTO,MultipartFile file) throws PlaceNotFoundException;
    void ratePlaceById(Long placeId, Long userId, Integer rating, boolean isRemoval) throws PlaceNotFoundException;
    void removeRating(Long placeId, Long userId) throws PlaceNotFoundException;
    void addPlaceToFavorites(Long placeId, Long userId);
}