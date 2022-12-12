package com.example.benomad.service;

import com.example.benomad.dto.PlanDTO;
import com.example.benomad.entity.Plan;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.security.request.GetPlanRequest;

import java.util.List;

public interface PlanService {
    List<PlanDTO> getAllPlans();
    PlanDTO getPlanById(Long planId);
    List<PlanDTO> getPlansByDay(Integer year, Integer month, Integer day);
    List<PlanDTO> getPlansByMonth(Integer year, Integer month);
    List<PlanDTO> getPlansByYear(Integer year);
    PlanDTO insertPlan(PlanDTO planDTO);
    PlanDTO updatePlanById(Long planId, PlanDTO planDTO);
    PlanDTO deletePlanById(Long planId);
    Plan getPlanEntityById(Long planId);
}
