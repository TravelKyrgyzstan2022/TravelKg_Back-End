
package com.example.benomad.service;

import com.example.benomad.exception.NotFoundException;
import com.example.benomad.dto.PlaceDTO;

import java.util.List;

public interface PlaceService {
    List<PlaceDTO> getAllPlaces();
    PlaceDTO getPlaceById(Long id) throws NotFoundException;
    PlaceDTO insertPlace(PlaceDTO placeDTO);
    PlaceDTO deletePlaceById(Long id) throws NotFoundException;
    PlaceDTO updatePlaceById(Long id,PlaceDTO placeDTO) throws NotFoundException;
}

