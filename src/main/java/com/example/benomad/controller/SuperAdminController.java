package com.example.benomad.controller;

import com.example.benomad.logger.LogWriterServiceImpl;
import com.example.benomad.service.impl.SuperServiceImpl;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/superadmin")
public class SuperAdminController {

    private final LogWriterServiceImpl logWriter;
    private final SuperServiceImpl superService;

    @Operation(summary = "Gets logs")
    @GetMapping(value = "/logs")
    public ResponseEntity<?> getLogs(){
        return ResponseEntity.ok(logWriter.getLog());
    }

    @Operation(summary = "Add administrator privileges to existing user")
    @PutMapping(value = "/new-admin")
    public ResponseEntity<?> makeAdmin(@RequestParam(name = "user_id") Long userId){
        return ResponseEntity.ok(superService.makeAdmin(userId));
    }

    @Operation(summary = "Remove administrator privileges from existing user")
    @PutMapping(value = "/remove-admin")
    public ResponseEntity<?> removeAdmin(@RequestParam(name = "user_id") Long userId){
        return ResponseEntity.ok(superService.removeAdmin(userId));
    }
}
