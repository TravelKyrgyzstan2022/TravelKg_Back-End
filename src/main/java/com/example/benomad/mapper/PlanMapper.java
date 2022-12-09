package com.example.benomad.mapper;

import com.example.benomad.dto.PlanDTO;
import com.example.benomad.entity.Plan;
import com.example.benomad.service.impl.PlaceServiceImpl;
import com.example.benomad.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanMapper {

    private final UserMapper userMapper;
    private final PlaceMapper placeMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PlanDTO entityToDto(Plan plan){
        return PlanDTO.builder()
                .id(plan.getId())
                .date(formatter.format(plan.getDate()))
                .user(userMapper.entityToDto(plan.getUser()))
                .place(placeMapper.entityToDto(plan.getPlace()))
                .note(plan.getNote())
                .build();
    }

    public Plan dtoToEntity(PlanDTO planDTO){
        return Plan.builder()
                .id(planDTO.getId())
                .date(LocalDate.parse(planDTO.getDate(), formatter))
                .note(planDTO.getNote())
                .build();
    }

    public List<PlanDTO> entityListToDtoList(List<Plan> entities){
        return entities.stream().map(this::entityToDto).collect(Collectors.toList());
    }
}
