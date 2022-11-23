package com.example.benomad.service;

import com.example.benomad.dto.PlanDTO;
import com.example.benomad.exception.ContentNotFoundException;

import java.util.List;

public interface PlanService {
    List<PlanDTO> getPlansByUserId(Long userId) throws ContentNotFoundException;
    PlanDTO getPlanById(Long planId) throws ContentNotFoundException;
    PlanDTO insertPlan(PlanDTO dto);
    PlanDTO updatePlan(PlanDTO dto, Long planId) throws ContentNotFoundException;
    PlanDTO deletePlanById(Long id);
}
