package com.example.benomad.service.impl;

import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.entity.Place;
import com.example.benomad.entity.Rating;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.enums.PlaceType;
import com.example.benomad.enums.Region;
import com.example.benomad.exception.ContentIsNotRatedException;
import com.example.benomad.exception.InvalidRatingException;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.logger.LogWriterServiceImpl;
import com.example.benomad.mapper.PlaceMapper;
import com.example.benomad.repository.PlaceRepository;
import com.example.benomad.repository.RatingRepository;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final PlaceMapper placeMapper;
    private final AuthServiceImpl authService;
    private final LogWriterServiceImpl logWriter;

    @Override
    public List<PlaceDTO> getPlacesByAttributes(String name, Region region, PlaceType placeType,
                                                   String address,Boolean match,PageRequest pageRequest) {
        Place builtPlace = Place.builder().name(name).address(address).region(region).placeType(placeType).build();
        Example<Place> exampleOfPlace = Example.of(builtPlace,getExampleMatcher(match));
        Page<Place> pages = placeRepository.findAll(exampleOfPlace,pageRequest);
        List<PlaceDTO> placeDTOS = placeMapper.entityListToDtoList(pages.stream().collect(Collectors.toList()));
        logWriter.get(String.format("%s - Returned %d places", authService.getName(), placeDTOS.size()));
        return placeDTOS;
    }


    @Override
    public PlaceDTO getPlaceById(Long placeId) throws ContentNotFoundException {
        PlaceDTO placeDTO = placeMapper.entityToDto(placeRepository.findById(placeId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.PLACE, "id", String.valueOf(placeId));
                })
        );
        logWriter.get(String.format("%s - Returned place with id = %d", authService.getName(), placeId));
        return placeDTO;
    }

    @Override
    public PlaceDTO insertPlace(PlaceDTO placeDTO) {
        placeDTO.setId(null);
        placeDTO.setId(placeRepository.save(placeMapper.dtoToEntity(placeDTO)).getId());
        logWriter.insert(String.format("%s - Inserted place with id = %d", authService.getName(), placeDTO.getId()));
        return placeDTO;
    }

    @Override
    public PlaceDTO deletePlaceById(Long placeId) throws ContentNotFoundException {
        Place place = placeRepository.findById(placeId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.PLACE, "id", String.valueOf(placeId));
                }
        );
        placeRepository.delete(place);
        logWriter.delete(String.format("%s - Deleted place with id = %d", authService.getName(), placeId));
        return placeMapper.entityToDto(place);
    }

    @Override
    public PlaceDTO updatePlaceById(Long placeId, PlaceDTO placeDTO) throws ContentNotFoundException {
        if(!placeRepository.existsById(placeId)){
            throw new ContentNotFoundException(ContentNotFoundEnum.PLACE, "id", String.valueOf(placeId));
        }
        placeRepository.save(placeMapper.dtoToEntity(placeDTO));
        logWriter.update(String.format("%s - Deleted place with id = %d", authService.getName(), placeId));
        return placeDTO;
    }

    @Override
    public PlaceDTO ratePlaceById(Long placeId, Integer rating, boolean isRemoval)
            throws ContentNotFoundException, InvalidRatingException {
        Long userId = authService.getCurrentUserId();
        if(isRemoval){
            rating = 1;
            Rating neededRating = ratingRepository.findByPlaceAndUser(
                    placeRepository.findById(placeId).orElseThrow(
                            () -> {
                                throw new ContentNotFoundException(ContentNotFoundEnum.PLACE, "id", String.valueOf(placeId));
                            }),
                    userRepository.findById(userId).orElseThrow(
                            () -> {
                                throw new ContentNotFoundException(ContentNotFoundEnum.USER, "id", String.valueOf(userId));
                            })
            ).orElseThrow(
                    () -> {
                        throw new ContentIsNotRatedException(ContentNotFoundEnum.PLACE);
                    });
            ratingRepository.delete(neededRating);
        }
        if(rating < 1 || rating > 5){
            throw new InvalidRatingException();
        }
        try{
            Rating neededRating = ratingRepository.findByPlaceAndUser(
                    placeRepository.findById(placeId).orElseThrow(
                            () -> {
                                throw new ContentNotFoundException(ContentNotFoundEnum.PLACE, "id", String.valueOf(placeId));
                            }),
                    userRepository.findById(userId).orElseThrow(
                            () -> {
                                throw new ContentNotFoundException(ContentNotFoundEnum.USER, "id", String.valueOf(userId));
                            }
                    )).orElseThrow(
                            () -> {
                                throw new ContentIsNotRatedException(ContentNotFoundEnum.PLACE);
                            });
            neededRating.setRating(rating);
            ratingRepository.save(neededRating);
        }catch (Exception e){
            ratingRepository.save(Rating.builder()
                    .place(placeRepository.findById(placeId).orElseThrow(
                            () -> {
                                throw new ContentNotFoundException(ContentNotFoundEnum.PLACE, "id", String.valueOf(placeId));
                            }
                    ))
                    .user(userRepository.findById(userId).orElseThrow(
                            () -> {
                                throw new ContentNotFoundException(ContentNotFoundEnum.USER, "id", String.valueOf(userId));
                            }
                    ))
                    .rating(rating)
                    .build());
        }
        logWriter.update(String.format("%s - %s place with id = %d", authService.getName(),
                isRemoval ? "Removed rated for" : "Rated", placeId));
        return placeMapper.entityToDto(placeRepository.findById(placeId).orElseThrow(() -> {
            throw new ContentNotFoundException(ContentNotFoundEnum.PLACE, "id", String.valueOf(placeId));
        }));
    }

    private ExampleMatcher getExampleMatcher(Boolean match) {
        ExampleMatcher matches = ExampleMatcher.matchingAll()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("region",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("placeType",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("address",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withIgnorePaths("id","description","imageUrl","linkUrl");
        ExampleMatcher notMatches = ExampleMatcher.matchingAny()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("region",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("placeType",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("address",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withIgnorePaths("id","description","imageUrl","linkUrl");
        return match ? matches : notMatches;
    }
}