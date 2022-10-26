package com.example.benomad.mapper;


import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.entity.Place;

public class PlaceMapper {
    public static Place placeDtoToPlace(PlaceDTO placeDTO) {
        return Place.builder()
                .id(placeDTO.getId())
                .name(placeDTO.getName())
                .region(RegionMapper.regionDtoToRegion(placeDTO.getRegionDTO()))
                .placeType(PlaceTypeMapper.placeTypeDtoToPlaceType(placeDTO.getPlaceTypeDTO()))
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
                .regionDTO(RegionMapper.regionToRegionDto(place.getRegion()))
                .placeTypeDTO(PlaceTypeMapper.placeTypeToPlaceTypeDto(place.getPlaceType()))
                .description(place.getDescription())
                .imageUrl(place.getImageUrl())
                .linkUrl(place.getLinkUrl())
                .address(place.getAddress())
                .build();
    }
}
