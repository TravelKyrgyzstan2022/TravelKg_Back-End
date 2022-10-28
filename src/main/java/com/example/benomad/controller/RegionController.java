package com.example.benomad.controller;


import com.example.benomad.dto.RegionDTO;
import com.example.benomad.service.impl.RegionServiceImpl;
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
@RequestMapping("/api/v1/regions")
@Tag(name = "Region resource", description = "The Region API ")
public class RegionController {
    private final RegionServiceImpl regionService;

    @GetMapping("")
    void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/v1/regions/");
    }

    @Operation(summary = "Gets all regions")
    @GetMapping( produces = "application/json")
    public ResponseEntity<?> getAllRegions(){
        return ResponseEntity.ok(regionService.getAllRegions());
    }

    @Operation(summary = "Finds region by ID")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getRegionById(@PathVariable Long id){
        return ResponseEntity.ok(regionService.getRegionById(id));
    }

    @Operation(summary = "Inserts region into the database")
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> insertRegion(@RequestBody RegionDTO regionDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(regionService.insertRegion(regionDTO));
    }

    @Operation(summary = "Deletes region by ID")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteRegionById(@PathVariable Long id){
        return ResponseEntity.ok(regionService.deleteRegionById(id));
    }

    @Operation(summary = "Updates region by ID")
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updatePlaceById(@PathVariable Long id, @RequestBody RegionDTO regionDTO){
        return ResponseEntity.ok(regionService.updateRegionById(id,regionDTO));
    }

}
