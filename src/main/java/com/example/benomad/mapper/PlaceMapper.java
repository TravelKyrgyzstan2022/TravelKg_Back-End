package com.example.benomad.mapper;


import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.entity.Place;
import com.example.benomad.enums.Content;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.repository.RatingRepository;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceMapper {

    private final RatingRepository ratingRepository;
    private final AuthServiceImpl authService;
    private final UserRepository userRepository;
    public Place dtoToEntity(PlaceDTO placeDTO) {

        return Place.builder()
                .id(placeDTO.getId())
                .name(placeDTO.getName())
                .region(placeDTO.getRegion())
                .placeType(placeDTO.getPlaceType())
                .description(placeDTO.getDescription())
                .imageUrls(placeDTO.getImageUrls())
                .linkUrl(placeDTO.getLinkUrl())
                .address(placeDTO.getAddress())
                .latitude(placeDTO.getLatitude())
                .longitude(placeDTO.getLongitude())
                .build();
    }

    public PlaceDTO entityToDto(Place place) {
        Long userId = authService.getCurrentUserId();
        return PlaceDTO.builder()
                .id(place.getId())
                .name(place.getName())
                .region(place.getRegion())
                .placeCategory(place.getPlaceCategory())
                .placeType(place.getPlaceType())
                .description(place.getDescription())
                .imageUrls(place.getImageUrls())
                .linkUrl(place.getLinkUrl())
                .address(place.getAddress())
                .averageRating(ratingRepository.findAverageRatingByPlaceId(place.getId()))
                .ratingCount(ratingRepository.findRatingCountByPlaceId(place.getId()))
                .isFavoriteOfCurrentUser(userId != null && userRepository.findById(userId).orElseThrow(
                        () -> new ContentNotFoundException(Content.USER, "userId", String.valueOf(userId))
                ).getPlaces().contains(place))
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .build();
    }

    public List<PlaceDTO> entityListToDtoList(List<Place> entities) {
        return entities.stream().map(this::entityToDto).collect(Collectors.toList());
    }
}
