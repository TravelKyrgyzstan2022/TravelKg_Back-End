package com.example.benomad.controller;

import com.example.benomad.advice.ExceptionResponse;
import com.example.benomad.dto.CommentDTO;
import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.service.impl.PlaceServiceImpl;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/places")
@Tag(name = "Admin Resource", description = "The Administrator API")
public class AdminPlaceController {

    private final PlaceServiceImpl placeService;

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


    @Operation(summary = "Inserts images by place id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = boolean.class))
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
    @PutMapping(value = "/{placeId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/json")
    public ResponseEntity<?> insertImagesByPlaceId(@PathVariable("placeId") Long placeId, @RequestParam("files") MultipartFile[] files){
        return ResponseEntity.ok(placeService.insertImagesByPlaceId(placeId,files));
    }

}
