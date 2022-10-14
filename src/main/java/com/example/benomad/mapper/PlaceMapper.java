package com.example.benomad.mapper;

import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.entity.Place;

import java.util.ArrayList;
import java.util.List;

public class PlaceMapper {
    public static PlaceDTO entityToDto(Place place){
        return PlaceDTO.builder()
                .id(place.getId())
                .build();
    }

    public static Place dtoToEntity(PlaceDTO placeDTO){
        return Place.builder()
                .id(placeDTO.getId())
                .build();
    }

    public static List<PlaceDTO> listOfEntitiesToListOfDtos(List<Place> places){
        List<PlaceDTO> dtos = new ArrayList<>();
        for(Place p: places){
            dtos.add(entityToDto(p));
        }
        return dtos;
    }
}
