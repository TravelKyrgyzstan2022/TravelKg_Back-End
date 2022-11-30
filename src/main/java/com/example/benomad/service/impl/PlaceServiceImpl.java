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
import com.example.benomad.entity.User;
import com.example.benomad.enums.*;
import com.example.benomad.exception.*;
import com.example.benomad.mapper.PlaceMapper;
import com.example.benomad.repository.PlaceRepository;
import com.example.benomad.repository.RatingRepository;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final PlaceMapper placeMapper;
    private final UserServiceImpl userService;
    private final AuthServiceImpl authService;
    private final LogWriterServiceImpl logWriter;
    private final ImageServiceImpl imageService;

    @Override
    public List<PlaceDTO> getMyFavorites() {
        Long userId = authService.getCurrentUserId();
        return placeMapper.entityListToDtoList(userService.getUserEntityById(userId).getPlaces());
    }

    @Override
    public List<PlaceDTO> getPlacesByAttributes(String name,
                                                   String address,Boolean match,PageRequest pageRequest) {
        Place builtPlace = Place.builder().name(name).address(address).build();
        Example<Place> exampleOfPlace = Example.of(builtPlace,getExampleMatcher(match));
        Page<Place> pages = placeRepository.findAll(exampleOfPlace,pageRequest);
        List<PlaceDTO> placeDTOS = placeMapper.entityListToDtoList(pages.stream().collect(Collectors.toList()));
        logWriter.get(String.format("%s - Returned %d places", authService.getCurrentEmail(), placeDTOS.size()));
        return placeDTOS;
    }


    @Override
    public PlaceDTO getPlaceById(Long placeId) throws ContentNotFoundException {
        PlaceDTO placeDTO = placeMapper.entityToDto(placeRepository.findById(placeId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.PLACE, "id", String.valueOf(placeId));
                })
        );
        logWriter.get(String.format("%s - Returned place with id = %d", authService.getCurrentEmail(), placeId));
        return placeDTO;
    }

    @Override
    public PlaceDTO insertPlace(PlaceDTO placeDTO) {
        placeDTO.setId(null);
        placeDTO.setId(placeRepository.save(placeMapper.dtoToEntity(placeDTO)).getId());
        logWriter.insert(String.format("%s - Inserted place with id = %d", authService.getCurrentEmail(), placeDTO.getId()));
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
        logWriter.delete(String.format("%s - Deleted place with id = %d", authService.getCurrentEmail(), placeId));
        return placeMapper.entityToDto(place);
    }

    @Override
    public PlaceDTO updatePlaceById(Long placeId, PlaceDTO placeDTO) throws ContentNotFoundException {
        if(!placeRepository.existsById(placeId)){
            throw new ContentNotFoundException(ContentNotFoundEnum.PLACE, "id", String.valueOf(placeId));
        }
        placeRepository.save(placeMapper.dtoToEntity(placeDTO));
        logWriter.update(String.format("%s - Deleted place with id = %d", authService.getCurrentEmail(), placeId));
        return placeDTO;
    }

    @Override
    public PlaceDTO ratePlaceById(Long placeId, Integer rating, boolean isRemoval)
            throws ContentNotFoundException, InvalidRatingException {
        Long userId = authService.getCurrentUserId();
        if(isRemoval){
            rating = 1;
            Rating neededRating = getRating(placeId, userId);
            ratingRepository.delete(neededRating);
        }
        if(rating < 1 || rating > 5){
            throw new InvalidRatingException();
        }
        try{
            Rating neededRating = getRating(placeId, userId);
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
        logWriter.update(String.format("%s - %s place with id = %d", authService.getCurrentEmail(),
                isRemoval ? "Removed rated for" : "Rated", placeId));
        return placeMapper.entityToDto(placeRepository.findById(placeId).orElseThrow(() -> {
            throw new ContentNotFoundException(ContentNotFoundEnum.PLACE, "id", String.valueOf(placeId));
        }));
    }

    private Rating getRating(Long placeId, Long userId) {
        return ratingRepository.findByPlaceAndUser(
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
    }

    @Override
    public List<PlaceDTO> getPlacesByTypesAndCategories(List<PlaceCategory> categories, List<PlaceType> types,List<Region> regions, Pageable pageable) {
        List<String> sCategories = categories.stream().map(Enum::name).collect(Collectors.toList());
        List<String> sTypes = types.stream().map(Enum::name).collect(Collectors.toList());
        List<String> sRegions = regions.stream().map(Enum::name).collect(Collectors.toList());
        Page<Place> placePage = placeRepository.findPlacesByPlaceCategoriesAndPlaceTypes(sCategories,sTypes,sRegions,pageable);
        return placeMapper.entityListToDtoList(placePage.stream().collect(Collectors.toList()));
    }

    @Override
    public boolean insertImagesByPlaceId(Long placeId, MultipartFile[] files) {
        Place place = placeRepository
                .findById(placeId).orElseThrow(
                        () -> new ContentNotFoundException(ContentNotFoundEnum.PLACE,"id",String.valueOf(placeId))
                );
        place.setImageUrls(imageService.uploadImages(files, ImagePath.PLACE));
        placeRepository.save(place);
        return true;
    }

    @Override
    public List<String> getImagesById(Long id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(
                        () -> new ContentNotFoundException(ContentNotFoundEnum.PLACE,"id",String.valueOf(id))
                );
        return place.getImageUrls();
    }

    @Override
    public boolean insertPlaceWithImages(PlaceDTO placeDTO, MultipartFile[] files) {
        placeDTO.setId(null);
        placeDTO.setImageUrls(imageService.uploadImages(files, ImagePath.PLACE));
        placeRepository.save(placeMapper.dtoToEntity(placeDTO));
        return true;
    }

    @Override
    public PlaceDTO addPlaceToFavorites(Long id) throws ContentNotFoundException {
        Long userId = authService.getCurrentUserId();
        /// FIXME: Throw not authorized exception
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ContentNotFoundException(ContentNotFoundEnum.USER,"id",String.valueOf(userId))
                );
        Place place = placeRepository.findById(id)
                .orElseThrow(
                        () -> new ContentNotFoundException(ContentNotFoundEnum.PLACE,"id",String.valueOf(id))
                );
        if (user.getPlaces().contains(place))  user.getPlaces().remove(place);
        else user.getPlaces().add(place);
        userRepository.save(user);
        return placeMapper.entityToDto(place);
    }

    private ExampleMatcher getExampleMatcher(Boolean match) {
        ExampleMatcher matches = ExampleMatcher.matchingAll()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("region",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("placeType",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("address",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withIgnorePaths("id","description","imageUrl","linkUrl","placeCategory","region");
        ExampleMatcher notMatches = ExampleMatcher.matchingAny()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("region",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("placeType",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("address",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withIgnorePaths("id","description","imageUrl","linkUrl","placeCategory","region");
        return match ? matches : notMatches;
    }
}