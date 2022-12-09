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
@RequestMapping("api/v1/user/support")
@Tag(name = "Support Resource", description = "The Support API ")
public class UserSupportController {

    private final SupportRequestServiceImpl supportService;

    // FIXME: 09.12.2022 do hidden forward slash fix and Support to Support Request

    @PostMapping(value = {"/{id}", ""})
    public ResponseEntity<?> insertSupport(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.CREATED).body(supportService.insertSupportRequest());
    }

}
