package com.example.benomad.controller;

import com.example.benomad.service.impl.SupportRequestServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/support/")
@Tag(name = "Support Resource", description = "The Support API ")
public class AdminSupportController {

    private final SupportRequestServiceImpl supportService;

    // FIXME: 09.12.2022 do hidden forward slash fix and Support to Support Request
    @GetMapping(value = {"", "/"}, produces = "application/json")
    public ResponseEntity<?> getAllSupports(){
        return ResponseEntity.ok(supportService.getAllSupportRequests());
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getSupportById(@PathVariable Long id){
        return ResponseEntity.ok(supportService.getSupportRequestById(id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteSupport(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(supportService.deleteSupportRequestById(id));
    }

}
