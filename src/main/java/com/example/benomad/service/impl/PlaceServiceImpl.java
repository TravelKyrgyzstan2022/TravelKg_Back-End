package com.example.benomad.service.impl;

import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.entity.Place;
import com.example.benomad.entity.Rating;
import com.example.benomad.entity.User;
import com.example.benomad.enums.PlaceCategory;
import com.example.benomad.enums.PlaceType;
import com.example.benomad.enums.Region;
import com.example.benomad.exception.InvalidRatingException;
import com.example.benomad.exception.PlaceNotFoundException;
import com.example.benomad.exception.UserNotFoundException;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final PlaceMapper placeMapper;

    @Override
    public List<PlaceDTO> getPlacesByAttributes(String name, Region region, PlaceType placeType, PlaceCategory placeCategory,
                                                   String address, Boolean match, Long currentUserId,PageRequest pageRequest) {
        Place builtPlace = Place.builder().name(name).address(address).region(region).placeType(placeType).placeCategory(placeCategory).build();
        Example<Place> exampleOfPlace = Example.of(builtPlace,getExampleMatcher(match));
        Page<Place> pages = placeRepository.findAll(exampleOfPlace,pageRequest);
        return placeMapper.entityListToDtoList(pages.stream().toList(),currentUserId);
    }


    @Override
    public PlaceDTO getPlaceById(Long id,Long currentUserId) throws PlaceNotFoundException {
        return placeMapper.entityToDto(placeRepository.findById(id).orElseThrow(PlaceNotFoundException::new),currentUserId);
    }

    @Override
    public PlaceDTO insertPlace(PlaceDTO placeDTO, MultipartFile file) {
        placeDTO.setId(null);
        placeRepository.save(placeMapper.dtoToEntity(placeDTO,file));
        return placeDTO;
    }

    @Override
    public PlaceDTO deletePlaceById(Long id) throws PlaceNotFoundException {
        Place place = placeRepository.findById(id).orElseThrow(PlaceNotFoundException::new);
        placeRepository.delete(place);
        return placeMapper.entityToDto(place,null);
    }

    @Override
    public PlaceDTO updatePlaceById(Long id, PlaceDTO placeDTO,MultipartFile file) throws PlaceNotFoundException {
        placeRepository.findById(id).orElseThrow(PlaceNotFoundException::new);
        placeRepository.save(placeMapper.dtoToEntity(placeDTO, file));
        return placeDTO;
    }

    @Override
    public void ratePlaceById(Long placeId, Long userId, Integer rating, boolean isRemoval)
            throws PlaceNotFoundException, InvalidRatingException {
        if(isRemoval){
            removeRating(placeId, userId);
            return;
        }
        if(rating < 1 || rating > 5){
            throw new InvalidRatingException();
        }
        try{
            //Checking if user have already rated the place or not
            //if yes then we will just update the rating instead of adding a new one
            Rating neededRating = ratingRepository.findByPlaceAndUser(placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new),
                    userRepository.findById(userId).orElseThrow(PlaceNotFoundException::new)).orElseThrow(PlaceNotFoundException::new);
            neededRating.setRating(rating);
            ratingRepository.save(neededRating);
        }catch (Exception e){
            //If program fails to find a rating in db we will create a new record
            ratingRepository.save(Rating.builder()
                    .place(placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new))
                    .user(userRepository.findById(userId).orElseThrow(PlaceNotFoundException::new))
                    .rating(rating)
                    .build());
        }

    }

    @Override
    public void removeRating(Long placeId, Long userId) throws PlaceNotFoundException{
        Rating neededRating = ratingRepository.findByPlaceAndUser(
                placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new),
                userRepository.findById(userId).orElseThrow(PlaceNotFoundException::new)
        ).orElseThrow(PlaceNotFoundException::new);
        ratingRepository.delete(neededRating);
    }

    public ExampleMatcher getExampleMatcher(Boolean match) {
        ExampleMatcher matches = ExampleMatcher.matchingAll()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("region",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("placeType",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("address",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("placeCategory",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withIgnorePaths("id","description","imageUrl","linkUrl");
        ExampleMatcher notMatches = ExampleMatcher.matchingAny()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("region",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("placeType",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("address",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withMatcher("placeCategory",ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase(true))
                .withIgnorePaths("id","description","imageUrl","linkUrl");
        return match ? matches : notMatches;
    }

    public void addPlaceToFavorites(Long placeId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Place place = placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new);
        user.getPlaces().add(place);
        userRepository.save(user);
    }
}