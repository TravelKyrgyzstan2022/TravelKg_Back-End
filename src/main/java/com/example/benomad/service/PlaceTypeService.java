package com.example.benomad.service;


import com.example.benomad.exception.NotFoundException;
import com.example.benomad.dto.PlaceTypeDTO;

import java.util.List;

public interface PlaceTypeService {
    List<PlaceTypeDTO> getAllPlaceTypes();
    PlaceTypeDTO getPlaceTypeById(Long id) throws NotFoundException;
    PlaceTypeDTO insertPlaceType(PlaceTypeDTO placeTypeDTO);
    PlaceTypeDTO deletePlaceTypeById(Long id) throws NotFoundException;
    PlaceTypeDTO updatePlaceTypeById(Long id, PlaceTypeDTO placeTypeDTO) throws NotFoundException;
}
