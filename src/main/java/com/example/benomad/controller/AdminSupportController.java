package com.example.benomad.controller;

import com.example.benomad.dto.SupportRequestDTO;
import com.example.benomad.service.impl.SupportRequestServiceImpl;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/support/")
@Tag(name = "Support Resource", description = "The Support API ")
public class AdminSupportController {

    private final SupportRequestServiceImpl supportService;

    @Operation(summary = "Get all support requests")
    @GetMapping(value = {""}, produces = "application/json")
    public ResponseEntity<List<SupportRequestDTO>> getAllSupportRequests(){
        return ResponseEntity.ok(supportService.getAllSupportRequests());
    }

    @Operation(summary = "Gets support request by ID")
    @GetMapping(value = "/{requestId}", produces = "application/json")
    public ResponseEntity<SupportRequestDTO> getSupportRequestById(@PathVariable("requestId") Long requestId){
        return ResponseEntity.ok(supportService.getSupportRequestById(requestId));
    }

    @Operation(summary = "Deletes support request by ID")
    @DeleteMapping(value = "/{requestId}")
    public ResponseEntity<SupportRequestDTO> deleteSupportRequestById(@PathVariable("requestId") Long requestId){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(supportService.deleteSupportRequestById(requestId));
    }
}
