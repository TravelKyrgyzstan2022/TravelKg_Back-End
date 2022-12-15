package com.example.benomad.service;

import com.example.benomad.dto.ImageDTO;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.entity.Comment;
import com.example.benomad.entity.Place;
import com.example.benomad.enums.PlaceCategory;
import com.example.benomad.enums.PlaceType;
import com.example.benomad.enums.Region;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.dto.PlaceDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface PlaceService {
    List<PlaceDTO> getMyFavorites();
    List<PlaceDTO> getPlacesByAttributes(String name, String address,Boolean match,PageRequest pageRequest);
    PlaceDTO getPlaceById(Long placeId);
    Long insertPlace(PlaceDTO placeDTO);
    PlaceDTO deletePlaceById(Long placeId);
    PlaceDTO updatePlaceById(Long placeId, PlaceDTO placeDTO);
    PlaceDTO ratePlaceById(Long placeId, Integer rating, boolean isRemoval);
    List<PlaceDTO> getPlacesByTypesAndCategories(List<PlaceCategory> categories,
                                                 List<PlaceType> types, List<Region> regions,Pageable pageable);
    PlaceDTO addPlaceToFavorites(Long placeId);
    PlaceDTO removePlaceFromFavorites(Long placeId);
    MessageResponse insertImagesByPlaceId(Long placeId, MultipartFile[] files);
    List<String> getImagesById(Long placeId);
    PlaceDTO insertPlaceWithImages(PlaceDTO placeDTO, MultipartFile[] files);
    MessageResponse insertImages64ByPLaceId(Long placeId, ImageDTO[] files);
    void addComment(Long placeId, Comment comment);
    Place getPlaceEntityById(Long placeId);
}