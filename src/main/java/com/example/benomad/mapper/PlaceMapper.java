package com.example.benomad.mapper;

import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.entity.Place;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.repository.PlaceRepository;
import com.example.benomad.repository.RatingRepository;
import com.example.benomad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceMapper {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;

    public Place dtoToEntity(PlaceDTO placeDTO) {

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

    public PlaceDTO entityToDto(Place place,Long currentUserId) {
        return PlaceDTO.builder()
                .id(place.getId())
                .name(place.getName())
                .region(place.getRegion())
                .placeType(place.getPlaceType())
                .placeCategory(place.getPlaceCategory())
                .description(place.getDescription())
                .imageUrl(place.getImageUrl().orElse(null))
                .linkUrl(place.getLinkUrl())
                .address(place.getAddress())
                .averageRating(ratingRepository.findAverageRatingByPlaceId(place.getId()))
                .ratingCount(ratingRepository.findRatingCountByPlaceId(place.getId()))
                .isFavoriteOfCurrentUser(userRepository.findById(currentUserId).orElseThrow(
                        () -> new ContentNotFoundException(ContentNotFoundEnum.USER,currentUserId))
                        .getPlaces().contains(place))
                .build();
    }

    public List<PlaceDTO> entityListToDtoList(List<Place> entities,Long currentUser) {
        return entities.stream().map(entity -> entityToDto(entity,currentUser)).collect(Collectors.toList());
    }

    public List<Place> dtoListToEntityList(List<PlaceDTO> dtos) {
        return (List<Place>) dtos.stream().map(this::dtoToEntity).collect(Collectors.toSet());
    }
}