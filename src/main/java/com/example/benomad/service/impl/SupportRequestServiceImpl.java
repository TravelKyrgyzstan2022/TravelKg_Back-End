package com.example.benomad.service.impl;

import com.example.benomad.dto.SupportRequestDTO;
import com.example.benomad.entity.SupportRequest;
import com.example.benomad.enums.Content;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.mapper.SupportRequestMapper;
import com.example.benomad.repository.SupportRequestRepository;
import com.example.benomad.service.SupportRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SupportRequestServiceImpl implements SupportRequestService {

    private final SupportRequestMapper supportRequestMapper;
    private final SupportRequestRepository supportRequestRepository;
    private final AuthServiceImpl authService;
    private final UserServiceImpl userService;

    @Override
    public List<SupportRequestDTO> getAllSupportRequests() {
        return supportRequestMapper.entityListToDtoList(supportRequestRepository.findAll());
    }

    @Override
    public SupportRequestDTO getSupportRequestById(Long requestId) {
        return supportRequestMapper.entityToDto(getSupportRequestEntityById(requestId));
    }

    @Override
    public SupportRequestDTO insertSupportRequest() {
        Long userId = authService.getCurrentUserId();
        SupportRequest supportRequest = new SupportRequest();
        supportRequest.setUser(userService.getUserEntityById(userId));
        supportRequest.setDate(LocalDateTime.now());
        supportRequestRepository.save(supportRequest);
        return supportRequestMapper.entityToDto(supportRequest);
    }

    @Override
    public SupportRequestDTO deleteSupportRequestById(Long requestId) {
        SupportRequest supportRequest = getSupportRequestEntityById(requestId);
        supportRequestRepository.delete(supportRequest);
        return supportRequestMapper.entityToDto(supportRequest);
    }

    @Override
    public SupportRequest getSupportRequestEntityById(Long requestId) {
        return supportRequestRepository.findById(requestId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(Content.SUPPORT, "id", String.valueOf(requestId));
                });
    }
}
