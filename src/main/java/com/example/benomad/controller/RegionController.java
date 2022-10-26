package com.example.benomad.controller;


import com.example.benomad.exception.NotFoundException;
import com.example.benomad.dto.RegionDTO;
import com.example.benomad.service.impl.RegionServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
@RequestMapping("/api/v1/regions")
@Tag(name = "Region resource", description = "The Region API ")
public class RegionController {
    private final RegionServiceImpl regionServiceImpl;

    @Operation(summary = "Get all regions")
    @GetMapping( produces = "application/json")
    public ResponseEntity<?> getAllRegions(){
        try{
            return ResponseEntity.ok(regionServiceImpl.getAllRegions());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Get region by id")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getRegionById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(regionServiceImpl.getRegionById(id));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Save region")
    @PostMapping(value = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> insertRegion(@RequestBody RegionDTO regionDTO){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(regionServiceImpl.insertRegion(regionDTO));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Delete region")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteRegionById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(regionServiceImpl.deleteRegionById(id));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Update region")
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updatePlaceById(@PathVariable Long id, @RequestBody RegionDTO regionDTO){
        try{
            return ResponseEntity.ok(regionServiceImpl.updateRegionById(id,regionDTO));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
