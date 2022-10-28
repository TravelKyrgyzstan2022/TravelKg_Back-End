package com.example.benomad.controller;

import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.enums.PlaceType;
import com.example.benomad.enums.Region;
import com.example.benomad.exception.InvalidRatingException;
import com.example.benomad.service.impl.PlaceServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RestController
@Data
@RequestMapping("/api/v1/places")
@Tag(name = "Place resource", description = "The Place API ")
public class PlaceController {
    private final PlaceServiceImpl placeService;

    @GetMapping("")
    void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/v1/places/");
    }

    @Operation(summary = "Get all places")
    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAllPlacesByAttributes(
            @RequestParam(name = "sort_by", required = false) Optional<String> sortBy,
            @RequestParam(name = "page", required = false) Optional<Integer> page,
            @RequestParam(name = "size", required = false) Optional<Integer> size,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "region", required = false) Region region,
            @RequestParam(name = "place_type", required = false) PlaceType placeType,
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "match", required = false,defaultValue = "false") Boolean match) {

        PageRequest pageRequest = PageRequest.of(page.orElse(0), size.orElse(1), Sort.by(sortBy.orElse("id")));
        return ResponseEntity.ok(placeService.getAllPlacesByAttributes(name, region, placeType,
                address, match, pageRequest));
    }

    @Operation(summary = "Get place by Id")
    @GetMapping(path = "/{id}",produces = "application/json")
    public ResponseEntity<?> getPlaceById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(placeService.getPlaceById(id));
    }

    @Operation(summary = "Delete place by Id")
    @PostMapping(value = "/" ,produces = "application/json")
    public ResponseEntity<?> savePlace(@RequestBody PlaceDTO placeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(placeService.insertPlace(placeDTO));
    }

    @Operation(summary = "Place by Id")
    @DeleteMapping(value = "/{id}",produces = "application/json")
    public ResponseEntity<?> deletePlaceById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(placeService.deletePlaceById(id));
    }

    @Operation(summary = "Update place")
    @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updatePlaceById(@PathVariable Long id, @RequestBody PlaceDTO placeDTO){
        return ResponseEntity.ok(placeService.updatePlaceById(id,placeDTO));
    }

    @PutMapping(value = "/rate")
    public ResponseEntity<?> ratePlace(@RequestParam(name = "place_id") Long placeId,
                                       @RequestParam(name = "user_id") Long userId,
                                       @RequestParam(name = "rating", defaultValue = "1") Integer rating,
                                       @RequestParam(name = "remove", defaultValue = "0") Boolean isRemoval){
        if(rating < 1 || rating > 5){
            throw new InvalidRatingException();
        }
        if(isRemoval){
            placeService.removeRating(placeId, userId);
        }else{
            placeService.ratePlaceById(placeId, userId, rating);
        }
        return ResponseEntity.ok(placeService.getPlaceById(placeId));
    }


}