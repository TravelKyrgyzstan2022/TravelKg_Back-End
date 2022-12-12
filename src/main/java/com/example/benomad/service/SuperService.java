package com.example.benomad.service;

import com.example.benomad.dto.MessageResponse;

public interface SuperService {
    MessageResponse makeAdmin(Long userId);
    MessageResponse removeAdmin(Long userId);
}
