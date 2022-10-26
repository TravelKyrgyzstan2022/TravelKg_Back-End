package com.example.benomad.service.impl;

import com.example.benomad.exception.NotFoundException;
import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.entity.Place;
import com.example.benomad.mapper.PlaceMapper;
import com.example.benomad.repository.PlaceRepository;
import com.example.benomad.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;
    @Override
    public List<PlaceDTO> getAllPlaces() {
        List<Place> places = placeRepository.findAll();
        List<PlaceDTO> placeDTOS = new ArrayList<>();
        for (Place place : places) {
            placeDTOS.add(PlaceMapper.placeToPlaceDto(place));
        }
        return placeDTOS;
    }

    @Override
    public PlaceDTO getPlaceById(Long id) throws NotFoundException {
        return PlaceMapper.placeToPlaceDto(placeRepository.findById(id).orElseThrow(NotFoundException::new));
    }

    @Override
    public PlaceDTO insertPlace(PlaceDTO placeDTO) {
        placeRepository.save(PlaceMapper.placeDtoToPlace(placeDTO));
        return placeDTO;
    }

    @Override
    public PlaceDTO deletePlaceById(Long id) throws NotFoundException {
        Place place = placeRepository.findById(id).orElseThrow(NotFoundException::new);
        placeRepository.delete(place);
        return PlaceMapper.placeToPlaceDto(place);
    }

    @Override
    public PlaceDTO updatePlaceById(Long id, PlaceDTO placeDTO) throws NotFoundException {
        placeRepository.findById(id).orElseThrow(NotFoundException::new);
        placeRepository.save(PlaceMapper.placeDtoToPlace(placeDTO));
        return placeDTO;
    }
}
