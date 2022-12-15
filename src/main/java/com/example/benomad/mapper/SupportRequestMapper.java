package com.example.benomad.mapper;

import com.example.benomad.dto.SupportRequestDTO;
import com.example.benomad.entity.SupportRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportRequestMapper {

    private final UserMapper userMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 'at' HH:mm");

    public SupportRequestDTO entityToDto(SupportRequest supportRequest){
        return SupportRequestDTO.builder()
                .id(supportRequest.getId())
                .user(userMapper.entityToDto(supportRequest.getUser()))
                .dateTime(formatter.format(supportRequest.getDate()))
                .build();
    }

    public List<SupportRequestDTO> entityListToDtoList(List<SupportRequest> entities){
        return entities.stream().map(this::entityToDto).collect(Collectors.toList());
    }
}
