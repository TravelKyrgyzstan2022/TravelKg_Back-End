package com.example.benomad.mapper;

import com.example.benomad.dto.DeletionInfoDTO;
import com.example.benomad.entity.DeletionInfo;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletionInfoMapper {

    private final UserRepository userRepository;

    public DeletionInfoDTO entityToDto(DeletionInfo info){
        return DeletionInfoDTO.builder()
                .id(info.getId())
                .deletionDate(info.getDeletionDate())
                .reason(info.getReason())
                .responsibleUserId(info.getResponsibleUser().getId())
                .build();
    }

    public DeletionInfo dtoToEntity(DeletionInfoDTO infoDTO){
        return DeletionInfo.builder()
                .id(infoDTO.getId())
                .deletionDate(infoDTO.getDeletionDate())
                .reason(infoDTO.getReason())
                .responsibleUser(userRepository.findById(infoDTO.getResponsibleUserId()).orElseThrow(
                        () -> new ContentNotFoundException(
                                ContentNotFoundEnum.USER, "id", String.valueOf(infoDTO.getResponsibleUserId())
                        )
                ))
                .build();
    }
}
