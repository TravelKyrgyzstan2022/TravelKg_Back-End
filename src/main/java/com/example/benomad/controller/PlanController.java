package com.example.benomad.controller;

import com.example.benomad.dto.PlanDTO;
import com.example.benomad.security.request.GetPlanRequest;
import com.example.benomad.service.impl.PlanServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user/plans")
@Tag(name = "Planner resource", description = "The Planner API")
public class PlanController {

    private final PlanServiceImpl planService;

//    @GetMapping(value = "/byUser/{userId}")
//    public ResponseEntity<?> getPlansByUserId(@PathVariable("userId") Long userId){
//        return ResponseEntity.ok(planService.getPlansByUserId(userId));
//    }

    @Operation(summary = "Get plan by ID (TEST)")
    @GetMapping(value = "/{planId}")
    public ResponseEntity<?> getPlanById(@PathVariable("planId") Long planId){
        return ResponseEntity.ok(planService.getPlanById(planId));
    }

    @Operation(summary = "Get plans by Date (TEST)")
    @PostMapping(value = {"/byDate/"})
    public ResponseEntity<?> getPlansByDate(@RequestBody GetPlanRequest request){
        return ResponseEntity.ok(planService.getPlansByDate(request));
    }

    @Operation(summary = "Insert plan by ID (TEST)")
    @PostMapping(value = {"/", ""})
    public ResponseEntity<?> insertPlan(@RequestBody PlanDTO planDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(planService.insertPlan(planDTO));
    }

    @Operation(summary = "Update plan by ID (TEST)")
    @PutMapping(value = {"/{planId}"})
    public ResponseEntity<?> updatePlan(@PathVariable("planId") Long planId, @RequestBody PlanDTO planDTO){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(planService.updatePlan(planDTO, planId));
    }

    @Operation(summary = "Delete plan by ID (TEST)")
    @DeleteMapping(value = "{planId}")
    public ResponseEntity<?> deletePlan(@PathVariable("planId") Long planId){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(planService.deletePlanById(planId));
    }

}