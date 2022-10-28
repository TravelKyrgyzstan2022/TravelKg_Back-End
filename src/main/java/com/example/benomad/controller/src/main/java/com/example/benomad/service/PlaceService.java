package com.example.benomad.service;

import com.example.benomad.enums.PlaceType;
import com.example.benomad.enums.Region;
import com.example.benomad.exception.NotFoundException;
import com.example.benomad.dto.PlaceDTO;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface PlaceService {
    List<PlaceDTO> getAllPlacesByAttributes( String name, Region region, PlaceType placeType, String address,Boolean match,PageRequest pageRequest) throws NotFoundException;
    PlaceDTO getPlaceById(Long id) throws NotFoundException;
    PlaceDTO insertPlace(PlaceDTO placeDTO);
    PlaceDTO deletePlaceById(Long id) throws NotFoundException;
    PlaceDTO updatePlaceById(Long id,PlaceDTO placeDTO) throws NotFoundException;
}
