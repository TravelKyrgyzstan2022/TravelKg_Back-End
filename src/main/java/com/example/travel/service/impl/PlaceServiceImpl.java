package com.example.travel.service.impl;

import com.example.travel.NotFoundException;
import com.example.travel.dto.PlaceDTO;
import com.example.travel.entity.Place;
import com.example.travel.mapper.PlaceMapper;
import com.example.travel.repository.PlaceRepository;
import com.example.travel.service.PlaceService;
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
        return PlaceMapper.placeToPlaceDto(placeRepository.findById(id).orElseThrow(()->new NotFoundException(String.format("This place with id %d not found",id))));
    }

    @Override
    public PlaceDTO insertPlace(PlaceDTO placeDTO) {
        placeRepository.save(PlaceMapper.placeDtoToPlace(placeDTO));
        return placeDTO;
    }

    @Override
    public PlaceDTO deletePlaceById(Long id) throws NotFoundException {
        Place place = placeRepository.findById(id).orElseThrow(()-> new NotFoundException(String.format("This place with id %d not found",id)));
        placeRepository.delete(place);
        return PlaceMapper.placeToPlaceDto(place);
    }

    @Override
    public PlaceDTO updatePlaceById(Long id, PlaceDTO placeDTO) throws NotFoundException {
        placeRepository.findById(id).orElseThrow(()->new NotFoundException(String.format("This place with id %d not found",id)));
        placeRepository.save(PlaceMapper.placeDtoToPlace(placeDTO));
        return placeDTO;
    }
}
