package com.example.benomad.controller;

import com.example.benomad.service.impl.SupportServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("api/v1/supports")
@Tag(name = "Support Resource", description = "The Support API ")
public class SupportController {

    private final SupportServiceImpl supportService;

    @GetMapping(value = {""}, produces = "application/json")
    public ResponseEntity<?> getAllSupports(){
        return ResponseEntity.ok(supportService.getAllSupports());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getSupportById(@PathVariable Long id){
        return ResponseEntity.ok(supportService.getSupportById(id));
    }


    @PostMapping(value = {"/{id}", ""})
    public ResponseEntity<?> insertSupport(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.CREATED).body(supportService.insertSupport(id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteSupport(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(supportService.deleteSupportById(id));
    }

}
