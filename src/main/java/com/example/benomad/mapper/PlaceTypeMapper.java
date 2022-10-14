package com.example.benomad.mapper;

import com.example.benomad.dto.PlaceTypeDTO;
import com.example.benomad.entity.PlaceType;

public class PlaceTypeMapper {
    public static PlaceType placeTypeDtoToPlaceType(PlaceTypeDTO placeTypeDTO) {
        return PlaceType.builder()
                .id(placeTypeDTO.getId())
                .name(placeTypeDTO.getName())
                .build();
    }
    public static PlaceTypeDTO placeTypeToPlaceTypeDto(PlaceType placeType) {
        return PlaceTypeDTO.builder()
                .id(placeType.getId())
                .name(placeType.getName())
                .build();
    }
}
