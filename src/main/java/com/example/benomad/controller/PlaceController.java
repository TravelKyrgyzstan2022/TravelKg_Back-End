package com.example.benomad.controller;

import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.enums.PlaceCategory;
import com.example.benomad.enums.PlaceType;
import com.example.benomad.enums.Region;
import com.example.benomad.service.impl.PlaceServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
@Tag(name = "Place resource", description = "The Place API ")
public class PlaceController {

    private final PlaceServiceImpl placeService;

    @Operation(summary = "Gets all places / Finds places by attributes")
    @GetMapping( value = {"/", ""})
    public ResponseEntity<?> getAllPlacesByAttributes(
            @RequestParam(name = "sort_by", required = false) Optional<String> sortBy,
            @RequestParam(name = "page", required = false) Optional<Integer> page,
            @RequestParam(name = "size", required = false) Optional<Integer> size,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "region", required = false) Region region,
            @RequestParam(name = "place_type", required = false) PlaceType placeType,
            @RequestParam(name = "place_category", required = false) PlaceCategory placeCategory,
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "match_all", required = false,defaultValue = "false") Boolean match,
            @RequestParam(name = "current_user_id", defaultValue = "1") Long userId) {

        PageRequest pageRequest = PageRequest.of(page.orElse(0), size.orElse(20), Sort.by(sortBy.orElse("id")));
        return ResponseEntity.ok(placeService.getPlacesByAttributes(name, region, placeType,placeCategory,
                address, match,userId, pageRequest));
    }

    @Operation(summary = "Gets place by ID")
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getPlaceById(@PathVariable("id") Long id,@RequestParam(value = "current_user_id", defaultValue = "1") Long userId) {
        return ResponseEntity.ok().body(placeService.getPlaceById(id,userId).getByteImage());
    }

    @Operation(summary = "Inserts a place to the database")
    @PostMapping(value = {"/",""})
    public ResponseEntity<?> savePlace(@ModelAttribute PlaceDTO placeDTO, @RequestParam MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(placeService.insertPlace(placeDTO,file));
    }

    @Operation(summary = "Deletes place by ID")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deletePlaceById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(placeService.deletePlaceById(id));
    }

    @Operation(summary = "Updates place by ID")
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updatePlaceById(@PathVariable Long id, @ModelAttribute PlaceDTO placeDTO,@RequestParam MultipartFile file){
        return ResponseEntity.ok(placeService.updatePlaceById(id,placeDTO,file));
    }

    @Operation(summary = "Rates place by ID",
    description = "Adds new rating record to a place by its ID and users ID.")
    @PutMapping(value = "/rate")
    public ResponseEntity<?> ratePlace(@RequestParam(name = "place_id") Long placeId,
                                       @RequestParam(name = "user_id") Long userId,
                                       @RequestParam(name = "rating", defaultValue = "1") Integer rating,
                                       @RequestParam(name = "remove", defaultValue = "0") Boolean isRemoval){

        placeService.ratePlaceById(placeId, userId, rating, isRemoval);
        return ResponseEntity.ok(placeService.getPlaceById(placeId,userId));
    }

    @Operation(summary = "Adds place to user favorites by ID",
            description = "Adds new favorite record to a place by its ID and users ID.")
    @PutMapping(value = "/favorite")
    public ResponseEntity<?> favoritePlace(@RequestParam(name = "place_id") Long placeId,
                                       @RequestParam(name = "user_id") Long userId){
        placeService.addPlaceToFavorites(placeId,userId);
        return ResponseEntity.ok(placeService.getPlaceById(placeId,userId));
    }




}