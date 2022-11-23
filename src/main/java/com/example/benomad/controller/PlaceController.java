package com.example.benomad.controller;

import com.example.benomad.advice.ExceptionResponse;
import com.example.benomad.dto.CommentDTO;
import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.dto.UserDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestParam(name = "region", required = false) Region region,
            @RequestParam(name = "place_type", required = false) PlaceType placeType,
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "match_all", required = false,defaultValue = "false") Boolean match) {

        PageRequest pageRequest = PageRequest.of(page.orElse(0), size.orElse(20), Sort.by(sortBy.orElse("id")));
        return ResponseEntity.ok(placeService.getPlacesByAttributes(name, region, placeType,
                address, match, pageRequest));
    }

    @Hidden
    @GetMapping(produces = "application/json", value = {"/"})
    public ResponseEntity<?> forwardSlashFix(
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

    @Operation(summary = "Inserts a place to the database")
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
    @PostMapping(value = {""}, produces = "application/json")
    public ResponseEntity<?> savePlace(@RequestBody PlaceDTO placeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(placeService.insertPlace(placeDTO));
    }

    @Hidden
    @PostMapping(value = {"/"}, produces = "application/json")
    public ResponseEntity<?> forwardSlashFix2(@RequestBody PlaceDTO placeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(placeService.insertPlace(placeDTO));
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

    @Operation(summary = "Deletes place by ID")
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
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deletePlaceById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(placeService.deletePlaceById(id));
    }

    @Operation(summary = "Updates place by ID")
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
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updatePlaceById(@PathVariable Long id, @RequestBody PlaceDTO placeDTO){
        return ResponseEntity.ok(placeService.updatePlaceById(id,placeDTO));
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
            @RequestParam(name = "types", required = false) Optional<List<PlaceType>> types)
    {
        PageRequest pageRequest = PageRequest.of(page.orElse(0), size.orElse(20), Sort.by(sortBy.orElse("id")));
        return ResponseEntity.ok(placeService.getPlacesByTypesAndCategories(categories.orElse(List.of(PlaceCategory.values())),types.orElse(List.of(PlaceType.values())),pageRequest));
    }

    @Operation(summary = "Uploads image by place ID",
            description = "Adds new image to a place by its ID and Image itself.")
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(schema = @Schema(implementation = Long.class))
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
    @PutMapping(path = "/uploadImage/{userId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadPlaceImage(@PathVariable("userId") Long id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(placeService.insertImageByPlaceId(id,file));
    }

    @Operation(summary = "Gets image by by place ID",
            description = "Adds new rating to a place by its ID and users ID.")
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
    @GetMapping(path = "/getImage/{userId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPlaceImage(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(placeService.getImageByPlaceId(id));
    }


    @Operation(summary = "Inserts place to current user's favorites",
            description = "Inserts place to current user's favorites using place id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))
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
    @PutMapping(value = "/favorites/{id}", produces = "application/json")
    public ResponseEntity<?> addPlaceToUser(@PathVariable Long id){
        return ResponseEntity.ok(placeService.addPlaceToFavorites(id));
    }



}