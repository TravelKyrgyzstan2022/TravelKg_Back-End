package com.example.benomad.mapper;


import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.entity.ByteImage;
import com.example.benomad.entity.Place;
import com.example.benomad.exception.UserNotFoundException;
import com.example.benomad.repository.RatingRepository;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.impl.ImageDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceMapper {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final ImageDataService imageDataService;

    public Place dtoToEntity(PlaceDTO placeDTO, MultipartFile file) {

        return Place.builder()
                .id(placeDTO.getId())
                .name(placeDTO.getName())
                .region(placeDTO.getRegion())
                .placeType(placeDTO.getPlaceType())
                .placeCategory(placeDTO.getPlaceCategory())
                .description(placeDTO.getDescription())
                .imageId(imageDataService.uploadImage(file))
                .linkUrl(placeDTO.getLinkUrl())
                .address(placeDTO.getAddress())
                .build();
    }

    public PlaceDTO entityToDto(Place place,Long currentUserId) {
        return PlaceDTO.builder()
                .id(place.getId())
                .name(place.getName())
                .region(place.getRegion())
                .placeType(place.getPlaceType())
                .placeCategory(place.getPlaceCategory())
                .description(place.getDescription())
                .byteImage(imageDataService.getImage(place.getImageId()))
                .linkUrl(place.getLinkUrl())
                .address(place.getAddress())
                .averageRating(ratingRepository.findAverageRatingByPlaceId(place.getId()))
                .ratingCount(ratingRepository.findRatingCountByPlaceId(place.getId()))
                .isFavoriteOfCurrentUser(userRepository.findById(currentUserId).orElseThrow(UserNotFoundException::new).getPlaces().contains(place))
                .build();
    }

    public List<PlaceDTO> entityListToDtoList(List<Place> places,Long currentUserId) {
        List<PlaceDTO> placeDTOS = new ArrayList<>();
        for (Place place : places) {
            placeDTOS.add(entityToDto(place,currentUserId));
        }
        return placeDTOS;
    }
}