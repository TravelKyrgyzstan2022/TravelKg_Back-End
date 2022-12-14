package com.example.benomad.controller;

import com.example.benomad.advice.ExceptionResponse;
import com.example.benomad.dto.ImageDTO;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.dto.UserDTO;
import com.example.benomad.service.impl.PlaceServiceImpl;
import com.example.benomad.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User Resource", description = "The User API ")
public class UserAccountController {

    private final UserServiceImpl userService;
    private final PlaceServiceImpl placeService;

    @Operation(summary = "Gets current user")
    @GetMapping(value = "/profile")
    public ResponseEntity<UserDTO> getCurrentUser(){
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @Operation(summary = "Updates current user")
    @PutMapping(value = "/profile")
    public ResponseEntity<UserDTO> updateCurrentUser(@Valid  @RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.updateCurrentUser(userDTO));
    }

    @Operation(summary = "Inserts current user image")
    @PutMapping(value = "/profile/image")
    public ResponseEntity<MessageResponse> insertUserImage(@RequestPart("file") MultipartFile file){
        return ResponseEntity.ok(userService.insertMyImage(file));
    }

    @Operation(summary = "Inserts current user image using base 64")
    @PutMapping(value = "/profile/image64")
    public ResponseEntity<?> insertUserImage64(@Valid @RequestBody ImageDTO file){
        return ResponseEntity.ok(userService.insertMyImage64(file));
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
    @PostMapping(value = "/favorites/{placeId}", produces = "application/json")
    public ResponseEntity<PlaceDTO> addPlaceToUser(@PathVariable("placeId") Long placeId){
        return ResponseEntity.ok(placeService.addPlaceToFavorites(placeId));
    }

    @Operation(summary = "Gets all favorite places of current user")
    @GetMapping(value = "/favorites", produces = "application/json")
    public ResponseEntity<List<PlaceDTO>> getUserFavorites(){
        return ResponseEntity.ok(placeService.getMyFavorites());
    }

    @Operation(summary = "Removes place from current user's favorites")
    @DeleteMapping(value = "/favorites/{placeId}")
    public ResponseEntity<PlaceDTO> removePlaceFromFavorites(@PathVariable("placeId") Long placeId){
        return ResponseEntity.ok(placeService.removePlaceFromFavorites(placeId));
    }
}
