package com.example.benomad.mapper;

import com.example.benomad.dto.SupportDTO;
import com.example.benomad.entity.Support;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportMapper {

    private final UserRepository userRepository;

    public SupportDTO entityToDto(Support support){
        return SupportDTO.builder()
                .id(support.getId())
                .userId(support.getUser().getId())
                .firstName(support.getUser().getFirstName())
                .lastName(support.getUser().getLastName())
                .email(support.getUser().getEmail())
                .date(support.getDate())
                .build();
    }

    public Support dtoToEntity(SupportDTO supportDTO){
        return Support.builder()
                .id(supportDTO.getId())
                .user(
                        userRepository.findById(supportDTO.getUserId()).orElseThrow(
                                () -> {
                                    throw new ContentNotFoundException(ContentNotFoundEnum.USER, "id", String.valueOf(supportDTO.getUserId()));
                                })
                )
                .date(supportDTO.getDate())
                .build();
    }

    public List<SupportDTO> entityListToDtoList(List<Support> entities){
        return entities.stream().map(this::entityToDto).collect(Collectors.toList());
    }
}
