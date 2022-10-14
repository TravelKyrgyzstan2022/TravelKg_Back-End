package com.example.travel.mapper;

import com.example.travel.dto.PlaceTypeDTO;
import com.example.travel.entity.PlaceType;

public class PlaceTypesMapper {
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
