package com.example.benomad.mapper;

import com.example.benomad.dto.PlanDTO;
import com.example.benomad.entity.Plan;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.repository.PlaceRepository;
import com.example.benomad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanMapper {
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    public PlanDTO entityToDto(Plan entity){
        return PlanDTO.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .userId(entity.getUser().getId())
                .placeId(entity.getPlace().getId())
                .note(entity.getNote())
                .build();
    }

    public Plan dtoToEntity(PlanDTO dto){
        return Plan.builder()
                .id(dto.getId())
                .user(userRepository.findById(dto.getUserId()).orElseThrow(
                        () -> {
                            throw new ContentNotFoundException(ContentNotFoundEnum.USER, "id", String.valueOf(dto.getUserId()));
                        }
                ))
                .place(placeRepository.findById(dto.getPlaceId()).orElseThrow(
                        () -> {
                            throw new ContentNotFoundException(ContentNotFoundEnum.PLACE,  "id", String.valueOf(dto.getPlaceId()));
                        }
                ))
                .date(dto.getDate())
                .note(dto.getNote())
                .build();
    }

    public List<PlanDTO> entityListToDtoList(List<Plan> entities){
        return entities.stream().map(this::entityToDto).collect(Collectors.toList());
    }
}
