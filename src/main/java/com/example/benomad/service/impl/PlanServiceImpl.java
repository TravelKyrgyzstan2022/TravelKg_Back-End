package com.example.benomad.service.impl;

import com.example.benomad.dto.PlanDTO;
import com.example.benomad.entity.Plan;
import com.example.benomad.entity.User;
import com.example.benomad.enums.Content;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.exception.NoAccessException;
import com.example.benomad.mapper.PlanMapper;
import com.example.benomad.repository.PlanRepository;
import com.example.benomad.security.request.GetPlanRequest;
import com.example.benomad.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanServiceImpl implements PlanService {

    private final AuthServiceImpl authService;
    private final PlanMapper planMapper;
    private final PlanRepository planRepository;
    private final UserServiceImpl userService;
    private final PlaceServiceImpl placeService;

    @Override
    public List<PlanDTO> getPlansByUserId(Long userId) throws ContentNotFoundException {
        User user = userService.getUserEntityById(userId);
        return planMapper.entityListToDtoList(planRepository.findByUser(user));
    }

    @Override
    public PlanDTO getPlanById(Long planId) throws ContentNotFoundException {
        return planMapper.entityToDto(getPlanEntityById(planId));
    }

    @Override
    // FIXME: 09.12.2022 full fixmne
    public List<PlanDTO> getPlansByDate(GetPlanRequest request){
        Long userId = authService.getCurrentUserId();
        userService.getUserEntityById(userId);
        return planMapper.entityListToDtoList(planRepository.findByDate(userId, request.getDate()));
    }

    @Override
    public PlanDTO insertPlan(PlanDTO planDTO, Long placeId) {
        Plan plan = planMapper.dtoToEntity(planDTO);
        plan.setId(null);
        plan.setUser(userService.getUserEntityById(authService.getCurrentUserId()));
        plan.setPlace(placeService.getPlaceEntityById(placeId));
        planRepository.save(plan);
        return planDTO;
    }

    @Override
    public PlanDTO updatePlan(PlanDTO planDTO, Long planId) throws ContentNotFoundException {
        checkPlan(planId);

        getPlanEntityById(planId);

        planDTO.setId(planId);
        planRepository.save(planMapper.dtoToEntity(planDTO));
        return planDTO;
    }

    @Override
    public PlanDTO deletePlanById(Long planId) {
        checkPlan(planId);

        Plan plan = getPlanEntityById(planId);
        planRepository.delete(plan);
        return planMapper.entityToDto(plan);
    }


    private void checkPlan(Long planId){
        User user = userService.getUserEntityById(authService.getCurrentUserId());
        Plan plan = getPlanEntityById(planId);
        if(!plan.getUser().getId().equals(user.getId())){
            throw new NoAccessException();
        }
    }

    private Plan getPlanEntityById(Long planId){
        return planRepository.findById(planId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(Content.PLAN, "id", String.valueOf(planId));
                });
    }
}
