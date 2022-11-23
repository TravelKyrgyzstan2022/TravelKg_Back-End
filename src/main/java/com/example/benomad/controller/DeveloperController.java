package com.example.benomad.controller;

import com.example.benomad.logger.LogWriterServiceImpl;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Hidden
@RequestMapping("/api/dev")
public class DeveloperController {

    private final LogWriterServiceImpl logWriter;

    @GetMapping(value = "/logs")
    public ResponseEntity<?> getLogs(){
        return ResponseEntity.ok(logWriter.getLog());
    }
}
