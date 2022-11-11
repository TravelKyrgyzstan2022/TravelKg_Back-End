package com.example.benomad.service.impl;

import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.entity.Place;
import com.example.benomad.entity.Rating;
import com.example.benomad.enums.*;
import com.example.benomad.exception.ContentIsNotRatedException;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.exception.FailedWhileUploadingException;
import com.example.benomad.exception.InvalidRatingException;
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
    private final ImageServiceImpl imageService;

    @Override
    public List<PlaceDTO> getPlacesByAttributes(String name, Region region, PlaceType placeType,
                                                   String address,Boolean match,PageRequest pageRequest) {
        Place builtPlace = Place.builder().name(name).address(address).region(region).placeType(placeType).build();
        Example<Place> exampleOfPlace = Example.of(builtPlace,getExampleMatcher(match));
        Page<Place> pages = placeRepository.findAll(exampleOfPlace,pageRequest);
        return placeMapper.entityListToDtoList(pages.stream().collect(Collectors.toList()));
    }


    @Override
    public PlaceDTO getPlaceById(Long placeId) throws ContentNotFoundException {
        return placeMapper.entityToDto(placeRepository.findById(placeId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.PLACE, placeId);
                })
        );
    }

    @Override
    public Long insertPlace(PlaceDTO placeDTO) {
        //we don't need to get lastValue
        //placeDTO.setId(placeRepository.save(placeMapper.dtoToEntity(placeDTO)).getId());
        placeDTO.setId(placeRepository.getLastValueOfSequence());
        return placeRepository.save(placeMapper.dtoToEntity(placeDTO)).getId();
    }

    @Override
    public PlaceDTO deletePlaceById(Long placeId) throws ContentNotFoundException {
        Place place = placeRepository.findById(placeId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.PLACE, placeId);
                }
        );
        placeRepository.delete(place);
        return placeMapper.entityToDto(place);
    }

    @Override
    public PlaceDTO updatePlaceById(Long placeId, PlaceDTO placeDTO) throws ContentNotFoundException {
        if(!placeRepository.existsById(placeId)){
            throw new ContentNotFoundException(ContentNotFoundEnum.PLACE, placeId);
        }
        placeRepository.save(placeMapper.dtoToEntity(placeDTO));
        return placeDTO;
    }

    @Override
    public PlaceDTO ratePlaceById(Long placeId, Long userId, Integer rating, boolean isRemoval)
            throws ContentNotFoundException, InvalidRatingException {
        if(isRemoval){
            rating = 1;
            Rating neededRating = ratingRepository.findByPlaceAndUser(
                    placeRepository.findById(placeId).orElseThrow(
                            () -> {
                                throw new ContentNotFoundException(ContentNotFoundEnum.PLACE, placeId);
                            }),
                    userRepository.findById(userId).orElseThrow(
                            () -> {
                                throw new ContentNotFoundException(ContentNotFoundEnum.USER, userId);
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
                                throw new ContentNotFoundException(ContentNotFoundEnum.PLACE, placeId);
                            }),
                    userRepository.findById(userId).orElseThrow(
                            () -> {
                                throw new ContentNotFoundException(ContentNotFoundEnum.USER, userId);
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
                                throw new ContentNotFoundException(ContentNotFoundEnum.PLACE, placeId);
                            }
                    ))
                    .user(userRepository.findById(userId).orElseThrow(
                            () -> {
                                throw new ContentNotFoundException(ContentNotFoundEnum.USER, userId);
                            }
                    ))
                    .rating(rating)
                    .build());
        }
        return placeMapper.entityToDto(placeRepository.findById(placeId).orElseThrow(() -> {
            throw new ContentNotFoundException(ContentNotFoundEnum.PLACE, placeId);
        }));
    }

    @Override
    public PlaceDTO insertImageByPlaceId(Long id, MultipartFile file) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException(ContentNotFoundEnum.PLACE,id));
        imageService.checkIsNotEmpty(file);
        imageService.checkIsImage(file);
        Map<String, String> metadata = imageService.getMetaData(file);
        String pathToFile = String.format("%s/%s", AwsBucket.MAIN_BUCKET.getBucketName(),ImagePath.PLACE.getPathToImage());
        String uniqueFileName = String.format("%s-%s",file.getOriginalFilename(), UUID.randomUUID());
        try {
            imageService.saveImageAws(pathToFile,uniqueFileName, Optional.of(metadata),file.getInputStream());
            place.setImageUrl(uniqueFileName);
            placeRepository.save(place);
            return placeMapper.entityToDto(place);
        } catch (IOException e) {
            throw new FailedWhileUploadingException();
        }
    }

    @Override
    public byte[] getImageByPlaceId(Long id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException(ContentNotFoundEnum.PLACE,id));
        String pathToFile = String.format("%s/%s", AwsBucket.MAIN_BUCKET.getBucketName(),ImagePath.PLACE.getPathToImage());
        return place.getImageUrl().map(x -> imageService.getAwsImageByPathAndKey(pathToFile,x)).orElse(new byte[0]);
    }

    public ExampleMatcher getExampleMatcher(Boolean match) {
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