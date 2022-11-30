package com.example.benomad.controller;

import com.example.benomad.advice.ExceptionResponse;
import com.example.benomad.dto.CommentDTO;
import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.enums.CommentReference;
import com.example.benomad.enums.PlaceCategory;
import com.example.benomad.enums.PlaceType;
import com.example.benomad.enums.Region;
import com.example.benomad.service.impl.CommentServiceImpl;
import com.example.benomad.service.impl.PlaceServiceImpl;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = PlaceDTO.class)))
            ),
            @ApiResponse(
                    responseCode = "Any error",
                    description = "Every response starting with 4** or 5** will have this body",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            )
    })
    @GetMapping(produces = "application/json", value = {""})
    public ResponseEntity<?> getAllPlacesByAttributes(
            @RequestParam(name = "sort_by", required = false) Optional<String> sortBy,
            @RequestParam(name = "page", required = false) Optional<Integer> page,
            @RequestParam(name = "size", required = false) Optional<Integer> size,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "match_all", required = false,defaultValue = "false") Boolean match) {

        PageRequest pageRequest = PageRequest.of(page.orElse(0), size.orElse(20), Sort.by(sortBy.orElse("id")));
        return ResponseEntity.ok(placeService.getPlacesByAttributes(name, address, match, pageRequest));
    }

    @Hidden
    @GetMapping(produces = "application/json", value = {"/"})
    public ResponseEntity<?> forwardSlashFix(
            @RequestParam(name = "sort_by", required = false) Optional<String> sortBy,
            @RequestParam(name = "page", required = false) Optional<Integer> page,
            @RequestParam(name = "size", required = false) Optional<Integer> size,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "match_all", required = false,defaultValue = "false") Boolean match) {

        PageRequest pageRequest = PageRequest.of(page.orElse(0), size.orElse(20), Sort.by(sortBy.orElse("id")));
        return ResponseEntity.ok(placeService.getPlacesByAttributes(name,address, match, pageRequest));
    }

    @Operation(summary = "Gets place by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = PlaceDTO.class))
            ),
            @ApiResponse(
                    responseCode = "Any error",
                    description = "Every response starting with 4** or 5** will have this body",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            )
    })
    @GetMapping(value = "/{id}",produces = "application/json")
    public ResponseEntity<?> getPlaceById(@PathVariable("id") Long placeId) {
        return ResponseEntity.ok().body(placeService.getPlaceById(placeId));
    }

    @Operation(summary = "Gets comments of place by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentDTO.class)))
            ),
            @ApiResponse(
                    responseCode = "Any error",
                    description = "Every response starting with 4** or 5** will have this body",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            )
    })
    @GetMapping(value = "/{id}/comments", produces = "application/json")
    public ResponseEntity<?> getPlaceCommentsById(
            @PathVariable("id") Long placeId,
            @RequestParam(name = "sort_by", required = false) Optional<String> sortBy,
            @RequestParam(name = "page", required = false) Optional<Integer> page,
            @RequestParam(name = "size", required = false) Optional<Integer> size){
        PageRequest pageRequest = PageRequest.of(page.orElse(0), size.orElse(1),
                Sort.by(sortBy.orElse("id")));
        return ResponseEntity.ok(commentService.getReferenceCommentsById(placeId, CommentReference.PLACE, pageRequest));
    }



    @Operation(summary = "Commenting place by ID")
    @PostMapping(value = "/{placeId}/comment", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = CommentDTO.class))
            ),
            @ApiResponse(
                    responseCode = "Any error",
                    description = "Every response starting with 4** or 5** will have this body",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            )
    })
    public ResponseEntity<?> commentPlace(@PathVariable Long placeId, @RequestBody CommentDTO commentDTO){
        return ResponseEntity.ok(commentService.insertComment(CommentReference.PLACE, placeId, commentDTO));
    }

    @Operation(summary = "Rates place by ID",
    description = "Adds new rating record to a place by its ID and users ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = CommentDTO.class))
            ),
            @ApiResponse(
                    responseCode = "Any error",
                    description = "Every response starting with 4** or 5** will have this body",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            )
    })
    @PutMapping(value = "/rate")
    public ResponseEntity<?> ratePlace(@RequestParam(name = "place_id") Long placeId,
                                       @RequestParam(name = "rating", defaultValue = "1") Integer rating,
                                       @RequestParam(name = "remove", defaultValue = "0") Boolean isRemoval){
        return ResponseEntity.ok(placeService.ratePlaceById(placeId, rating, isRemoval));
    }

    @Operation(summary = "Gets all places by place type and place category",
            description = "Get all places that refer to specific category and type")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = PlaceDTO.class))
            ),
            @ApiResponse(
                    responseCode = "Any error",
                    description = "Every response starting with 4** or 5** will have this body",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            )
    })
    @GetMapping(produces = "application/json", value = {"/filter", "filter"})
    public ResponseEntity<?> getAllPlacesByTypesAndCategories(
            @RequestParam(name = "sort_by", required = false) Optional<String> sortBy,
            @RequestParam(name = "page", required = false) Optional<Integer> page,
            @RequestParam(name = "size", required = false) Optional<Integer> size,
            @RequestParam(name = "categories", required = false) Optional<List<PlaceCategory>> categories,
            @RequestParam(name = "types", required = false) Optional<List<PlaceType>> types,
            @RequestParam(name = "regions", required = false) Optional<List<Region>> regions)
    {
        PageRequest pageRequest = PageRequest.of(page.orElse(0), size.orElse(20), Sort.by(sortBy.orElse("id")));
        return ResponseEntity.ok(placeService.getPlacesByTypesAndCategories(categories.orElse(List.of(PlaceCategory.values())),types.orElse(List.of(PlaceType.values())),regions.orElse(List.of(Region.values())),pageRequest));
    }

    @Operation(summary = "Gets all images by place id",
            description = "Get all images by id that refers to specific image")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = List.class))
            ),
            @ApiResponse(
                    responseCode = "Any error",
                    description = "Every response starting with 4** or 5** will have this body",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            )
    })
    @GetMapping(value = {"/{placeId}/images"})
    public ResponseEntity<?> getImagesById(@PathVariable("placeId") Long id) {
        return ResponseEntity.ok(placeService.getImagesById(id));
    }
}