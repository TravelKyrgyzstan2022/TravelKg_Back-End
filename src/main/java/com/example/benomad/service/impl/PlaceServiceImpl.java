package com.example.benomad.service.impl;

import com.example.benomad.dto.ImageDTO;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.entity.Comment;
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
import com.example.benomad.exception.FavoritePlaceException;
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
    public List<PlaceDTO> getPlacesByAttributes(String name, String address, Boolean MATCH_ALL,
                                                PageRequest pageRequest) {
        Place builtPlace = Place.builder().name(name).address(address).build();
        Example<Place> exampleOfPlace = Example.of(builtPlace,getExampleMatcher(MATCH_ALL));
        Page<Place> pages = placeRepository.findAll(exampleOfPlace,pageRequest);
        return placeMapper.entityListToDtoList(pages.stream().collect(Collectors.toList()));
    }


    @Override
    public PlaceDTO getPlaceById(Long placeId){
        return placeMapper.entityToDto(getPlaceEntityById(placeId));
    }

    @Override
    public Long insertPlace(PlaceDTO placeDTO) {
        placeDTO.setId(null);
        return placeRepository.save(placeMapper.dtoToEntity(placeDTO)).getId();
    }

    @Override
    public PlaceDTO deletePlaceById(Long placeId) {
        Place place = getPlaceEntityById(placeId);
        placeRepository.delete(place);
        return placeMapper.entityToDto(place);
    }

    @Override
    public PlaceDTO updatePlaceById(Long placeId, PlaceDTO placeDTO) {
        getPlaceEntityById(placeId);
        placeDTO.setId(placeId);
        placeRepository.save(placeMapper.dtoToEntity(placeDTO));
        return placeDTO;
    }

    @Override
    public PlaceDTO ratePlaceById(Long placeId, Integer rating, boolean isRemoval) {
        Long userId = authService.getCurrentUserId();
        if (isRemoval) {
            Rating neededRating = getRating(placeId, userId);
            ratingRepository.delete(neededRating);
            return placeMapper.entityToDto(getPlaceEntityById(placeId));
        }
        if (rating < 1 || rating > 5) {
            throw new InvalidRatingException();
        }
        try {
            Rating neededRating = getRating(placeId, userId);
            neededRating.setRating(rating);
            ratingRepository.save(neededRating);
        } catch (Exception e) {
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
    public List<PlaceDTO> getPlacesByTypesAndCategories(List<PlaceCategory> categories, List<PlaceType> types,
                                                        List<Region> regions, Pageable pageable) {
        List<String> sCategories = categories.stream().map(Enum::name).collect(Collectors.toList());
        List<String> sTypes = types.stream().map(Enum::name).collect(Collectors.toList());
        List<String> sRegions = regions.stream().map(Enum::name).collect(Collectors.toList());
        Page<Place> placePage = placeRepository.findPlacesByPlaceCategoriesAndPlaceTypes(sCategories,sTypes,sRegions,pageable);
        return placeMapper.entityListToDtoList(placePage.stream().collect(Collectors.toList()));
    }

    @Override
    public MessageResponse insertImagesByPlaceId(Long placeId, MultipartFile[] files) {
        Place place = getPlaceEntityById(placeId);
        place.setImageUrls(imageService.uploadImages(files, ImagePath.PLACE));
        placeRepository.save(place);
        return new MessageResponse("Images have been successfully added to the place!", 200);
    }

    @Override
    public List<String> getImagesById(Long placeId) {
        Place place = getPlaceEntityById(placeId);
        return place.getImageUrls();
    }

    @Override
    public PlaceDTO insertPlaceWithImages(PlaceDTO placeDTO, MultipartFile[] files) {
        placeDTO.setId(null);
        placeDTO.setImageUrls(imageService.uploadImages(files, ImagePath.PLACE));
        placeDTO.setId(placeRepository.save(placeMapper.dtoToEntity(placeDTO)).getId());
        return placeDTO;
    }

    @Override
    public MessageResponse insertImages64ByPLaceId(Long id, ImageDTO[] files) {
        Place place = getPlaceEntityById(id);
        place.setImageUrls(imageService.uploadImages64(files,ImagePath.PLACE));
        placeRepository.save(place);
        return new MessageResponse("Images have been successfully added to the place!", 200);
    }

    @Override
    public PlaceDTO addPlaceToFavorites(Long placeId) {
        User user = userService.getUserEntityById(authService.getCurrentUserId());
        Place place = getPlaceEntityById(placeId);
        if (user.getPlaces().contains(place)) {
            throw new FavoritePlaceException(true);
        }
        user.getPlaces().add(place);
        userRepository.save(user);
        return placeMapper.entityToDto(place);
    }

    @Override
    public PlaceDTO removePlaceFromFavorites(Long placeId) {
        User user = userService.getUserEntityById(authService.getCurrentUserId());
        Place place = getPlaceEntityById(placeId);
        if (!user.getPlaces().contains(place)) {
            throw new FavoritePlaceException(false);
        }
        user.getPlaces().remove(place);
        userRepository.save(user);
        return placeMapper.entityToDto(place);
    }
    @Override
    public void addComment(Long placeId, Comment comment) {
        Place place = getPlaceEntityById(placeId);
        place.getComments().add(comment);
        placeRepository.save(place);
    }

    @Override
    public Place getPlaceEntityById(Long placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(
                        () -> new ContentNotFoundException(Content.PLACE,"id",String.valueOf(placeId))
                );
    }
    
    @Override
    public List<PlaceDTO> getTopByCategories(Integer limit, List<PlaceCategory> categories) {
        return placeMapper.entityListToDtoList(placeRepository.findTop5PlacesForPlaceCategory(categories.stream().map(Enum::name).toList(),limit));
    }
    
    
    private ExampleMatcher getExampleMatcher(Boolean MATCH_ALL) {
        ExampleMatcher MATCHER_ALL = ExampleMatcher.matchingAll()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("region",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("placeType",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("address",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withIgnorePaths("id","description","imageUrl","linkUrl","placeCategory","region","latitude","longitude");
        ExampleMatcher MATCHER_ANY = ExampleMatcher.matchingAny()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("region",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("placeType",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("address",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withIgnorePaths("id","description","imageUrl","linkUrl","placeCategory","region","latitude","longitude");
        return MATCH_ALL ? MATCHER_ALL : MATCHER_ANY;
    }
}
