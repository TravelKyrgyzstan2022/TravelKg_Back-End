package com.example.benomad.mapper;


import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.entity.Place;
import com.example.benomad.repository.RatingRepository;

import java.util.ArrayList;
import java.util.List;

public class PlaceMapper {
    public static Place dtoToEntity(PlaceDTO placeDTO) {

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

    public static PlaceDTO entityToDto(Place place, RatingRepository ratingRepository) {
        return PlaceDTO.builder()
                .id(place.getId())
                .name(place.getName())
                .region(place.getRegion())
                .placeType(place.getPlaceType())
                .description(place.getDescription())
                .imageUrl(place.getImageUrl())
                .linkUrl(place.getLinkUrl())
                .address(place.getAddress())
                .averageRating(ratingRepository.findAverageRatingByPlaceId(place.getId()))
                .ratingCount(ratingRepository.findRatingCountByPlaceId(place.getId()))
                .build();
    }

    public static PlaceDTO entityToDto(Place place) {
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

    public static List<PlaceDTO> entityListToDtoList(List<Place> places, RatingRepository ratingRepository) {
        List<PlaceDTO> placeDTOS = new ArrayList<>();
        for (Place place : places) {
            placeDTOS.add(entityToDto(place, ratingRepository));
        }
        return placeDTOS;
    }
}