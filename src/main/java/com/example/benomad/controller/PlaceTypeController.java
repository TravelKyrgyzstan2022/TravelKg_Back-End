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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Data
@RequestMapping("/api/v1/placetypes")
@Tag(name = "Place Types resource", description = "The Place Types API ")
public class PlaceTypeController {
    private final PlaceTypeServiceImpl placeTypeService;

    @GetMapping("")
    void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/v1/placetypes/");
    }

    @Operation(summary = "Gets all place types")
    @GetMapping( produces = "application/json")
    public ResponseEntity<?> getAllPlaceTypes(){
        try{
            return ResponseEntity.ok(placeTypeService.getAllPlaceTypes());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Gets place type by ID")
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

    @Operation(summary = "Inserts place type into the database")
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> insertPlaceType(@RequestBody PlaceTypeDTO placeTypeDTO){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(placeTypeService.insertPlaceType(placeTypeDTO));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Deletes place type by ID")
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

    @Operation(summary = "Updates place type by ID")
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
