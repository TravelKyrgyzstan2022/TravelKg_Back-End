package com.example.benomad.service;

import com.example.benomad.dto.SupportRequestDTO;
import com.example.benomad.entity.SupportRequest;
import com.example.benomad.exception.ContentNotFoundException;

import java.util.List;

public interface SupportRequestService {
    List<SupportRequestDTO> getAllSupportRequests();
    SupportRequestDTO getSupportRequestById(Long id);
    SupportRequestDTO insertSupportRequest();
    SupportRequestDTO deleteSupportRequestById(Long id);
    SupportRequest getSupportRequestEntityById(Long requestId);
}
