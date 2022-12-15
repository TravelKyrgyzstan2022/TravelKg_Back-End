package com.example.benomad.service.impl;

import com.example.benomad.dto.PlanDTO;
import com.example.benomad.entity.Plan;
import com.example.benomad.entity.User;
import com.example.benomad.enums.Content;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.exception.InvalidDateException;
import com.example.benomad.exception.NoAccessException;
import com.example.benomad.mapper.PlanMapper;
import com.example.benomad.repository.PlanRepository;
import com.example.benomad.security.domain.Role;
import com.example.benomad.service.PlanService;
import com.example.benomad.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
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
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public List<PlanDTO> getAllPlans() {
        User user = userService.getUserEntityById(authService.getCurrentUserId());
        return planMapper.entityListToDtoList(planRepository.findByUser(user));
    }

    @Override
    public PlanDTO getPlanById(Long planId) {
        checkPlan(planId);
        return planMapper.entityToDto(getPlanEntityById(planId));
    }

    @Override
    public List<PlanDTO> getPlansByDay(Integer year, Integer month, Integer day) {
        String dateString = DateUtils.integerToDateString(day) + "/" + DateUtils.integerToDateString(month) + "/" + year;
        return planMapper.entityListToDtoList(
                planRepository.findByDate(authService.getCurrentUserId(), DateUtils.parseDate(dateString, formatter)));
    }

    @Override
    public List<PlanDTO> getPlansByMonth(Integer year, Integer month) {
        String dateString = "01/" + DateUtils.integerToDateString(month) + "/" + year;
        return planMapper.entityListToDtoList(
                planRepository.findByMonth(authService.getCurrentUserId(), DateUtils.parseDate(dateString, formatter)));
    }

    @Override
    public List<PlanDTO> getPlansByYear(Integer year) {
        String dateString = "01/01/" + year;
        return planMapper.entityListToDtoList(
                planRepository.findByYear(authService.getCurrentUserId(), DateUtils.parseDate(dateString, formatter)));
    }

    @Override
    public PlanDTO insertPlan(PlanDTO planDTO) {
        Plan plan = planMapper.dtoToEntity(planDTO);
        plan.setId(null);
        plan.setUser(userService.getUserEntityById(authService.getCurrentUserId()));
        plan.setPlace(placeService.getPlaceEntityById(planDTO.getPlaceId()));
        return planMapper.entityToDto(planRepository.save(plan));
    }

    @Override
    public PlanDTO updatePlanById(Long planId, PlanDTO planDTO) {
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

    @Override
    public Plan getPlanEntityById(Long planId) {
        return planRepository.findById(planId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(Content.PLAN, "id", String.valueOf(planId));
                });
    }

    private void checkPlan(Long planId) {
        User user = userService.getUserEntityById(authService.getCurrentUserId());
        Plan plan = getPlanEntityById(planId);
        if (!plan.getUser().getId().equals(user.getId())
                && !user.getRoles().contains(Role.ROLE_ADMIN)
                && !user.getRoles().contains(Role.ROLE_SUPERADMIN)) {
            throw new NoAccessException();
        }
    }
}
