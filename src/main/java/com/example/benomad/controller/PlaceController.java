package com.example.benomad.controller;

import com.example.benomad.dto.CommentDTO;
import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.enums.CommentReference;
import com.example.benomad.enums.PlaceType;
import com.example.benomad.enums.Region;
import com.example.benomad.service.impl.CommentServiceImpl;
import com.example.benomad.service.impl.PlaceServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
@Tag(name = "Place resource", description = "The Place API ")
public class PlaceController {

    private final PlaceServiceImpl placeService;
    private final CommentServiceImpl commentService;

    @Operation(summary = "Gets all places / Finds places by attributes")
    @GetMapping(produces = "application/json", value = {"/", ""})
    public ResponseEntity<?> getAllPlacesByAttributes(
            @RequestParam(name = "sort_by", required = false) Optional<String> sortBy,
            @RequestParam(name = "page", required = false) Optional<Integer> page,
            @RequestParam(name = "size", required = false) Optional<Integer> size,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "region", required = false) Region region,
            @RequestParam(name = "place_type", required = false) PlaceType placeType,
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "match_all", required = false,defaultValue = "false") Boolean match) {

        PageRequest pageRequest = PageRequest.of(page.orElse(0), size.orElse(20), Sort.by(sortBy.orElse("id")));
        return ResponseEntity.ok(placeService.getPlacesByAttributes(name, region, placeType,
                address, match, pageRequest));
    }

    @Operation(summary = "Gets place by ID")
    @GetMapping(value = "/{id}",produces = "application/json")
    public ResponseEntity<?> getPlaceById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(placeService.getPlaceById(id));
    }

    @Operation(summary = "Gets comments of place by ID")
    @GetMapping(value = "/{id}/comments", produces = "application/json")
    public ResponseEntity<?> getPlaceCommentsById(
            @PathVariable Long id,
//            @RequestParam(name = "current_user_id", defaultValue = "1") Long userid,
            @RequestParam(name = "sort_by", required = false) Optional<String> sortBy,
            @RequestParam(name = "page", required = false) Optional<Integer> page,
            @RequestParam(name = "size", required = false) Optional<Integer> size,
            Principal principal){
        PageRequest pageRequest = PageRequest.of(page.orElse(0), size.orElse(1),
                Sort.by(sortBy.orElse("id")));
        return ResponseEntity.ok(commentService.getReferenceCommentsById(principal, id, CommentReference.PLACE, pageRequest));
    }

    @Operation(summary = "Inserts a place to the database")
    @PostMapping(value = {"/",""}, produces = "application/json")
    public ResponseEntity<?> savePlace(@RequestBody PlaceDTO placeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(placeService.insertPlace(placeDTO));
    }

    @PostMapping(value = "/{id}/comment", produces = "application/json")
    public ResponseEntity<?> commentPlace(@PathVariable Long placeId, @RequestBody CommentDTO commentDTO){
        return ResponseEntity.ok(commentService.insertComment(CommentReference.PLACE, placeId, commentDTO));
    }

    @Operation(summary = "Deletes place by ID")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deletePlaceById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(placeService.deletePlaceById(id));
    }

    @Operation(summary = "Updates place by ID")
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updatePlaceById(@PathVariable Long id, @RequestBody PlaceDTO placeDTO){
        return ResponseEntity.ok(placeService.updatePlaceById(id,placeDTO));
    }

    @Operation(summary = "Rates place by ID",
    description = "Adds new rating record to a place by its ID and users ID.")
    @PutMapping(value = "/rate")
    public ResponseEntity<?> ratePlace(@RequestParam(name = "place_id") Long placeId,
//                                       @RequestParam(name = "user_id") Long userId,
                                       @RequestParam(name = "rating", defaultValue = "1") Integer rating,
                                       @RequestParam(name = "remove", defaultValue = "0") Boolean isRemoval,
                                       Principal principal){
        return ResponseEntity.ok(placeService.ratePlaceById(placeId, principal, rating, isRemoval));
    }


}