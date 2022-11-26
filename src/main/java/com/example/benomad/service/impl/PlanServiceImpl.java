package com.example.benomad.service.impl;

import com.example.benomad.dto.ArticleDTO;
import com.example.benomad.dto.PlanDTO;
import com.example.benomad.entity.Plan;
import com.example.benomad.entity.User;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.logger.LogWriterServiceImpl;
import com.example.benomad.mapper.PlanMapper;
import com.example.benomad.repository.PlanRepository;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final AuthServiceImpl authService;
    private final PlanMapper planMapper;
    private final PlanRepository planRepository;
    private final LogWriterServiceImpl logWriter;
    private final UserRepository userRepository;

    @Override
    public List<PlanDTO> getPlansByUserId(Long userId) throws ContentNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.USER, "id", String.valueOf(userId));
                }
        );
        List<PlanDTO> dtos = planMapper.entityListToDtoList(planRepository.findByUser(user));
        logWriter.get(String.format("%s - Returned %d plans", authService.getCurrentEmail(), dtos.size()));
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
    public PlanDTO insertPlan(PlanDTO planDTO) {
        planRepository.save(planMapper.dtoToEntity(planDTO));
        logWriter.insert(String.format("%s - Inserted plan with id = %d", authService.getCurrentEmail(), planDTO.getId()));
        return planDTO;
    }

    @Override
    public PlanDTO updatePlan(PlanDTO planDTO, Long planId) throws ContentNotFoundException {
        if(!planRepository.existsById(planId)){
            throw new ContentNotFoundException(ContentNotFoundEnum.PLAN, "id", String.valueOf(planId));
        }

        planDTO.setId(planId);
        planRepository.save(planMapper.dtoToEntity(planDTO));
        logWriter.update(String.format("%s - Updated plan with id = %d", authService.getCurrentEmail(), planId));
        return planDTO;
    }

    @Override
    public PlanDTO deletePlanById(Long planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.PLAN, "id", String.valueOf(planId));
                });

        planRepository.delete(plan);
        logWriter.delete(String.format("%s - Deleted plan with id = %d", authService.getCurrentEmail(), planId));
        return planMapper.entityToDto(plan);
    }
}
