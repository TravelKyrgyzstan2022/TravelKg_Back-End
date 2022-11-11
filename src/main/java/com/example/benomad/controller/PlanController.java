package com.example.benomad.controller;

import com.example.benomad.dto.PlanDTO;
import com.example.benomad.service.impl.PlanServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users/{userId}/plans")
public class PlanController {

    private final PlanServiceImpl planService;

    @GetMapping(value = {"", "/"})
    public ResponseEntity<?> getPlansByUserId(@PathVariable("userId") Long userId){
        return ResponseEntity.ok(planService.getPlansByUserId(userId));
    }

    @GetMapping(value = "/{planId}")
    public ResponseEntity<?> getPlanById(@PathVariable("planId") Long planId){
        return ResponseEntity.ok(planService.getPlanById(planId));
    }

    @PostMapping(value = {"/", ""})
    public ResponseEntity<?> insertPlan(@RequestBody PlanDTO planDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(planService.insertPlan(planDTO));
    }

    @PutMapping(value = {"/{planId}"})
    public ResponseEntity<?> updatePlan(@PathVariable("planId") Long planId, @RequestBody PlanDTO planDTO){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(planService.updatePlan(planDTO, planId));
    }

    @DeleteMapping(value = "{planId}")
    public ResponseEntity<?> deletePlan(@PathVariable("planId") Long planId){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(planService.deletePlanById(planId));
    }

}