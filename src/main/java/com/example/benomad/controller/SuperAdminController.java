package com.example.benomad.controller;

import com.example.benomad.dto.MessageResponse;
import com.example.benomad.service.impl.SuperServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/superadmin")
public class SuperAdminController {

    private final SuperServiceImpl superService;

    @Operation(summary = "Add administrator privileges to existing user")
    @PutMapping(value = "/new-admin")
    public ResponseEntity<MessageResponse> makeAdmin(@RequestParam(name = "user_id") Long userId){
        return ResponseEntity.ok(superService.makeAdmin(userId));
    }

    @Operation(summary = "Remove administrator privileges from existing user")
    @PutMapping(value = "/remove-admin")
    public ResponseEntity<MessageResponse> removeAdmin(@RequestParam(name = "user_id") Long userId){
        return ResponseEntity.ok(superService.removeAdmin(userId));
    }
}
