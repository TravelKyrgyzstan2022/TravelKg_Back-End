package com.example.benomad.service.impl;

import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.entity.Place;
import com.example.benomad.enums.PlaceType;
import com.example.benomad.enums.Region;
import com.example.benomad.exception.NotFoundException;
import com.example.benomad.mapper.PlaceMapper;
import com.example.benomad.repository.PlaceRepository;
import com.example.benomad.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;
    @Override
    public List<PlaceDTO> getAllPlacesByAttributes(String name, Region region, PlaceType placeType, String address,Boolean match,PageRequest pageRequest) {
        Place builtPlace = Place.builder().name(name).address(address).region(region).placeType(placeType).build();
        Example<Place> exampleOfPlace = Example.of(builtPlace,getExampleMatcher(match));
        Page<Place> pages = placeRepository.findAll(exampleOfPlace,pageRequest);
        return PlaceMapper.placeListToPlaceDtoList(pages.stream().toList());
    }


    @Override
    public PlaceDTO getPlaceById(Long id) throws NotFoundException {
        return PlaceMapper.placeToPlaceDto(placeRepository.findById(id).orElseThrow(NotFoundException::new));
    }

    @Override
    public PlaceDTO insertPlace(PlaceDTO placeDTO) {
        placeRepository.save(PlaceMapper.placeDtoToPlace(placeDTO));
        return placeDTO;
    }

    @Override
    public PlaceDTO deletePlaceById(Long id) throws NotFoundException {
        Place place = placeRepository.findById(id).orElseThrow(NotFoundException::new);
        placeRepository.delete(place);
        return PlaceMapper.placeToPlaceDto(place);
    }

    @Override
    public PlaceDTO updatePlaceById(Long id, PlaceDTO placeDTO) throws NotFoundException {
        placeRepository.findById(id).orElseThrow(NotFoundException::new);
        placeRepository.save(PlaceMapper.placeDtoToPlace(placeDTO));
        return placeDTO;
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
        return match ?  matches : notMatches;
    }
}
