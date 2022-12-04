package com.example.benomad.service.impl;

import com.example.benomad.dto.SupportDTO;
import com.example.benomad.entity.Support;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.logger.LogWriterServiceImpl;
import com.example.benomad.mapper.SupportMapper;
import com.example.benomad.repository.SupportRepository;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.SupportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportServiceImpl implements SupportService {

    private final SupportMapper supportMapper;
    private final SupportRepository supportRepository;
    private final AuthServiceImpl authService;
    private final LogWriterServiceImpl logWriter;
    private final UserRepository userRepository;

    @Override
    public List<SupportDTO> getAllSupports() {
        List<SupportDTO> dtos = supportMapper.entityListToDtoList(supportRepository.findAll());
        logWriter.get(String.format("%s - Returned %d supports", authService.getCurrentEmail(), dtos.size()));
        return dtos;
    }

    @Override
    public SupportDTO getSupportById(Long id) throws ContentNotFoundException {
        logWriter.get(String.format("%s - Returned support with id = %d", authService.getCurrentEmail(), id));
        return supportMapper.entityToDto(supportRepository.findById(id).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.SUPPORT, "id", String.valueOf(id));
                })
        );
    }

    @Override
    public SupportDTO insertSupport(Long id) {
        Support support = new Support();
        support.setUser(userRepository.findById(id).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.USER, "id", String.valueOf(id));
                }
        ));
        support.setDate(LocalDateTime.now());
        supportRepository.save(support);
        logWriter.insert(String.format("%s - Inserted support with id = %d", authService.getCurrentEmail(), supportMapper.entityToDto(support).getId()));
        return supportMapper.entityToDto(support);
    }

    @Override
    public SupportDTO deleteSupportById(Long supportId) {
        Support support = supportRepository.findById(supportId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.SUPPORT, "id", String.valueOf(supportId));
                });

        supportRepository.delete(support);
        logWriter.delete(String.format("%s - Deleted support with id = %d", authService.getCurrentEmail(), supportId));
        return supportMapper.entityToDto(support);
    }
}
