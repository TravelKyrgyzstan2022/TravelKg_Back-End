
package com.example.benomad.controller;


import com.example.benomad.exception.NotFoundException;
import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.service.impl.PlaceServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
@RequestMapping("/api/v1/places/")
@Tag(name = "Place resource", description = "The Place API ")
public class PlaceController {
    private final PlaceServiceImpl placeServiceImpl;

    @Operation(summary = "Gets all places")
    @GetMapping( produces = "application/json")
    public ResponseEntity<?> getAllPlaces(){
        try{
            return ResponseEntity.ok(placeServiceImpl.getAllPlaces());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Finds place by ID")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getPlaceById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(placeServiceImpl.getPlaceById(id));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Inserts place into the database")
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> insertPlace(@RequestBody PlaceDTO placeDTO){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(placeServiceImpl.insertPlace(placeDTO));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Deletes place by ID")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deletePlaceById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(placeServiceImpl.deletePlaceById(id));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Updates place by ID")
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updatePlaceById(@PathVariable Long id, @RequestBody PlaceDTO placeDTO){
        try{
            return ResponseEntity.ok(placeServiceImpl.updatePlaceById(id,placeDTO));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

