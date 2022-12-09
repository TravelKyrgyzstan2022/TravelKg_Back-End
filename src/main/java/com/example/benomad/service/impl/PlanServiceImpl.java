package com.example.benomad.service.impl;

import com.example.benomad.dto.PlanDTO;
import com.example.benomad.entity.Plan;
import com.example.benomad.entity.User;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.exception.NoAccessException;
import com.example.benomad.mapper.PlanMapper;
import com.example.benomad.repository.PlanRepository;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.security.request.GetPlanRequest;
import com.example.benomad.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final AuthServiceImpl authService;
    private final PlanMapper planMapper;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final UserServiceImpl userService;

    @Override
    public List<PlanDTO> getPlansByUserId(Long userId) throws ContentNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.USER, "id", String.valueOf(userId));
                }
        );
        List<PlanDTO> dtos = planMapper.entityListToDtoList(planRepository.findByUser(user));
        return dtos;
    }

    @Override
    public PlanDTO getPlanById(Long planId) throws ContentNotFoundException {
        return planMapper.entityToDto(planRepository.findById(planId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.PLAN, "id", String.valueOf(planId));
                })
        );
    }

    @Override
    public List<PlanDTO> getPlansByDate(GetPlanRequest request){
        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.USER, "id", String.valueOf(request.getUserId()));
                }
        );
        List<PlanDTO> dtos = planMapper.entityListToDtoList(planRepository.findByDate(request.getUserId(), request.getDate()));
        return dtos;
    }

    @Override
    public PlanDTO insertPlan(PlanDTO planDTO) {
        planRepository.save(planMapper.dtoToEntity(planDTO));
        return planDTO;
    }

    @Override
    public PlanDTO updatePlan(PlanDTO planDTO, Long planId) throws ContentNotFoundException {
        checkPlan(planId);

        if(!planRepository.existsById(planId)){
            throw new ContentNotFoundException(ContentNotFoundEnum.PLAN, "id", String.valueOf(planId));
        }

        planDTO.setId(planId);
        planRepository.save(planMapper.dtoToEntity(planDTO));
        return planDTO;
    }

    @Override
    public PlanDTO deletePlanById(Long planId) {
        checkPlan(planId);

        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.PLAN, "id", String.valueOf(planId));
                });

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
                    throw new ContentNotFoundException(ContentNotFoundEnum.PLAN, "id", String.valueOf(planId));
                });
    }
}
