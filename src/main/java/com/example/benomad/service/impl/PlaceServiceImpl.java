package com.example.benomad.service.impl;

import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.entity.Place;
import com.example.benomad.entity.Rating;
import com.example.benomad.enums.Content;
import com.example.benomad.enums.PlaceType;
import com.example.benomad.enums.Region;
import com.example.benomad.exception.ContentIsNotRatedException;
import com.example.benomad.exception.InvalidRatingException;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.entity.User;
import com.example.benomad.enums.*;
import com.example.benomad.mapper.PlaceMapper;
import com.example.benomad.repository.PlaceRepository;
import com.example.benomad.repository.RatingRepository;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final PlaceMapper placeMapper;
    private final UserServiceImpl userService;
    private final AuthServiceImpl authService;
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
        return placeMapper.entityListToDtoList(pages.stream().collect(Collectors.toList()));
    }


    @Override
    public PlaceDTO getPlaceById(Long placeId) throws ContentNotFoundException {
        return placeMapper.entityToDto(getPlaceEntityById(placeId));
    }

    @Override
    public PlaceDTO insertPlace(PlaceDTO placeDTO) {
        placeDTO.setId(null);
        placeDTO.setId(placeRepository.save(placeMapper.dtoToEntity(placeDTO)).getId());
        return placeDTO;
    }

    @Override
    public PlaceDTO deletePlaceById(Long placeId) throws ContentNotFoundException {
        Place place = getPlaceEntityById(placeId);
        placeRepository.delete(place);
        return placeMapper.entityToDto(place);
    }

    @Override
    public PlaceDTO updatePlaceById(Long placeId, PlaceDTO placeDTO) throws ContentNotFoundException {
        getPlaceEntityById(placeId);
        placeDTO.setId(placeId);
        placeRepository.save(placeMapper.dtoToEntity(placeDTO));
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
                    .place(getPlaceEntityById(placeId))
                    .user(userService.getUserEntityById(userId))
                    .rating(rating)
                    .build());
        }
        return placeMapper.entityToDto(getPlaceEntityById(placeId));
    }

    private Rating getRating(Long placeId, Long userId) {
        return ratingRepository.findByPlaceAndUser(
                getPlaceEntityById(placeId),
                userService.getUserEntityById(userId)
        ).orElseThrow(
                () -> {
                    throw new ContentIsNotRatedException(Content.PLACE);
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
        Place place = getPlaceEntityById(placeId);
        place.setImageUrls(imageService.uploadImages(files, ImagePath.PLACE));
        placeRepository.save(place);
        return true;
    }

    @Override
    public List<String> getImagesById(Long placeId) {
        Place place = getPlaceEntityById(placeId);
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
    public PlaceDTO addPlaceToFavorites(Long placeId) throws ContentNotFoundException {
        Long userId = authService.getCurrentUserId();
        User user = userService.getUserEntityById(userId);
        Place place = getPlaceEntityById(placeId);
        if (user.getPlaces().contains(place))  user.getPlaces().remove(place);
        else user.getPlaces().add(place);
        userRepository.save(user);
        return placeMapper.entityToDto(place);
    }

    public Place getPlaceEntityById(Long placeId){
        return placeRepository.findById(placeId)
                .orElseThrow(
                        () -> new ContentNotFoundException(Content.PLACE,"id",String.valueOf(placeId))
                );
    }
    
    private ExampleMatcher getExampleMatcher(Boolean match) {
        ExampleMatcher matches = ExampleMatcher.matchingAll()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("region",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("placeType",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("address",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withIgnorePaths("id","description","imageUrl","linkUrl","placeCategory","region","latitude","longitude");
        ExampleMatcher notMatches = ExampleMatcher.matchingAny()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("region",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("placeType",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("address",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withIgnorePaths("id","description","imageUrl","linkUrl","placeCategory","region","latitude","longitude");
        return match ? matches : notMatches;
    }
}
