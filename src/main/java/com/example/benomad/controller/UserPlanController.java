package com.example.benomad.controller;

import com.example.benomad.dto.DateDTO;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.dto.PlanDTO;
import com.example.benomad.service.impl.PlanServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user/plans")
@Tag(name = "Planner resource", description = "The Planner API")
public class UserPlanController {

    private final PlanServiceImpl planService;

    @GetMapping("")
    @Operation(summary = "Get all plans of current user")
    public ResponseEntity<List<PlanDTO>> getAllPlans(){
        return ResponseEntity.ok(planService.getAllPlans());
    }

    @GetMapping(value = "/dates")
    @Operation(summary = "Get all dates of plans")
    public ResponseEntity<List<DateDTO>> getAllDates(){
        return ResponseEntity.ok(planService.getAllDates());
    }

    @Operation(summary = "Get plan by ID")
    @GetMapping(value = "/{planId}")
    public ResponseEntity<PlanDTO> getPlanById(@PathVariable("planId") Long planId){
        return ResponseEntity.ok(planService.getPlanById(planId));
    }

    @Operation(summary = "Get plans by exact date")
    @GetMapping(value = {"/by-date/{year}/{month}/{day}"})
    public ResponseEntity<List<PlanDTO>> getPlansByDate(@PathVariable("year") Integer year,
                                                        @PathVariable("month") Integer month,
                                                        @PathVariable("day") Integer day){
        return ResponseEntity.ok(planService.getPlansByDay(year, month, day));
    }

    @Operation(summary = "Get plans by month")
    @GetMapping(value = {"/by-date/{year}/{month}"})
    public ResponseEntity<List<PlanDTO>> getPlansByDate(@PathVariable("year") Integer year,
                                            @PathVariable("month") Integer month){
        return ResponseEntity.ok(planService.getPlansByMonth(year, month));
    }

    @Operation(summary = "Get plans by year")
    @GetMapping(value = {"/by-date/{year}"})
    public ResponseEntity<List<PlanDTO>> getPlansByDate(@PathVariable("year") Integer year){
        return ResponseEntity.ok(planService.getPlansByYear(year));
    }

    @Operation(summary = "Insert plan", description = "Format in which date should be passed : 30/01/2020")
    @PostMapping(value = {""})
    public ResponseEntity<PlanDTO> insertPlan(@Valid @RequestBody PlanDTO planDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(planService.insertPlan(planDTO));
    }

    @Operation(summary = "Update plan by ID")
    @PutMapping(value = {"/{planId}"})
    public ResponseEntity<PlanDTO> updatePlan(@PathVariable("planId") Long planId, @RequestBody PlanDTO planDTO){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(planService.updatePlanById(planId, planDTO));
    }

    @Operation(summary = "Delete plan by ID")
    @DeleteMapping(value = "/{planId}")
    public ResponseEntity<PlanDTO> deletePlan(@PathVariable("planId") Long planId){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(planService.deletePlanById(planId));
    }

}