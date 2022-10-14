package com.example.benomad.mapper;


import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.entity.Place;

public class PlaceMapper {
    public static Place dtoToEntity(PlaceDTO placeDTO) {
        return Place.builder()
                .id(placeDTO.getId())
                .name(placeDTO.getName())
                .region(RegionMapper.dtoToEntity(placeDTO.getRegionDTO()))
                .placeType(PlaceTypeMapper.dtoToEntity(placeDTO.getPlaceTypeDTO()))
                .description(placeDTO.getDescription())
                .imageUrl(placeDTO.getImageUrl())
                .linkUrl(placeDTO.getLinkUrl())
                .address(placeDTO.getAddress())
                .build();
    }

    public static PlaceDTO entityToDto(Place place) {
        return PlaceDTO.builder()
                .id(place.getId())
                .name(place.getName())
                .regionDTO(RegionMapper.entityToDto(place.getRegion()))
                .placeTypeDTO(PlaceTypeMapper.entityToDto(place.getPlaceType()))
                .description(place.getDescription())
                .imageUrl(place.getImageUrl())
                .linkUrl(place.getLinkUrl())
                .address(place.getAddress())
                .build();
    }
}
