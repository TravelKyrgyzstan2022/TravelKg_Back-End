package com.example.travel.service;

import com.example.travel.NotFoundException;
import com.example.travel.dto.PlaceDTO;

import java.util.List;

public interface PlaceService {
    List<PlaceDTO> getAllPlaces();
    PlaceDTO getPlaceById(Long id) throws NotFoundException;
    PlaceDTO insertPlace(PlaceDTO placeDTO);
    PlaceDTO deletePlaceById(Long id) throws NotFoundException;
    PlaceDTO updatePlaceById(Long id,PlaceDTO placeDTO) throws NotFoundException;
}
