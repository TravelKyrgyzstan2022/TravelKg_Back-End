package com.example.benomad.controller;

import com.example.benomad.exception.NotFoundException;
import com.example.benomad.dto.PlaceTypeDTO;
import com.example.benomad.service.impl.PlaceTypeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
@RequestMapping("/api/v1/placetypes")
@Tag(name = "Place Types resource", description = "The Place Types API ")
public class PlaceTypeController {
    private final PlaceTypeServiceImpl placeTypeService;

    @Operation(summary = "Get all place types")
    @GetMapping( produces = "application/json")
    public ResponseEntity<?> getAllPlaceTypes(){
        try{
            return ResponseEntity.ok(placeTypeService.getAllPlaceTypes());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Get place Type by id")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getPlaceTypeById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(placeTypeService.getPlaceTypeById(id));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Save place type")
    @PostMapping(value = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> insertPlaceType(@RequestBody PlaceTypeDTO placeTypeDTO){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(placeTypeService.insertPlaceType(placeTypeDTO));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Delete place type")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deletePlaceTypeById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(placeTypeService.deletePlaceTypeById(id));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Update place type")
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updatePlaceTypeById(@PathVariable Long id, @RequestBody PlaceTypeDTO placeTypeDTO){
        try{
            return ResponseEntity.ok(placeTypeService.updatePlaceTypeById(id,placeTypeDTO));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
