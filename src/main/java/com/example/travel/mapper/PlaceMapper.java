package com.example.travel.mapper;


import com.example.travel.dto.PlaceDTO;
import com.example.travel.entity.Place;

public class PlaceMapper {
    public static Place placeDtoToPlace(PlaceDTO placeDTO) {
        return Place.builder()
                .id(placeDTO.getId())
                .name(placeDTO.getName())
                .region(placeDTO.getRegion())
                .placeType(placeDTO.getPlaceType())
                .description(placeDTO.getDescription())
                .imageUrl(placeDTO.getImageUrl())
                .linkUrl(placeDTO.getLinkUrl())
                .address(placeDTO.getAddress())
                .build();
    }

    public static PlaceDTO placeToPlaceDto(Place place) {
        return PlaceDTO.builder()
                .id(place.getId())
                .name(place.getName())
                .region(place.getRegion())
                .placeType(place.getPlaceType())
                .description(place.getDescription())
                .imageUrl(place.getImageUrl())
                .linkUrl(place.getLinkUrl())
                .address(place.getAddress())
                .build();
    }
}
