package com.example.benomad.service;

import com.example.benomad.enums.PlaceType;
import com.example.benomad.enums.Region;
import com.example.benomad.exception.PlaceNotFoundException;
import com.example.benomad.dto.PlaceDTO;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface PlaceService {
    List<PlaceDTO> getAllPlacesByAttributes( String name, Region region, PlaceType placeType, String address,Boolean match,PageRequest pageRequest) throws PlaceNotFoundException;
    PlaceDTO getPlaceById(Long id) throws PlaceNotFoundException;
    PlaceDTO insertPlace(PlaceDTO placeDTO);
    PlaceDTO deletePlaceById(Long id) throws PlaceNotFoundException;
    PlaceDTO updatePlaceById(Long id, PlaceDTO placeDTO) throws PlaceNotFoundException;
    void ratePlaceById(Long placeId, Long userId, Integer rating, boolean isRemoval) throws PlaceNotFoundException;
    void removeRating(Long placeId, Long userId) throws PlaceNotFoundException;
}