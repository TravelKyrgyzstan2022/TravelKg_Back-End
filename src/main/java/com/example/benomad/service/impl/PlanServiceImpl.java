package com.example.benomad.service.impl;

import com.example.benomad.dto.PlanDTO;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.service.PlanService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {
    @Override
    public List<PlanDTO> getPlansByUserId(Long userId) throws ContentNotFoundException {
        return null;
    }

    @Override
    public PlanDTO getPlanById(Long planId) throws ContentNotFoundException {
        return null;
    }

    @Override
    public PlanDTO insertPlan(PlanDTO dto) {
        return null;
    }

    @Override
    public PlanDTO updatePlan(PlanDTO dto, Long planId) throws ContentNotFoundException {
        return null;
    }

    @Override
    public PlanDTO deletePlanById(Long id) {
        return null;
    }
}
