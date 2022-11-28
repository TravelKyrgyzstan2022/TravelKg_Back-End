package com.example.benomad.service.impl;

import com.example.benomad.dto.MessageResponse;
import com.example.benomad.entity.User;
import com.example.benomad.security.domain.Role;
import com.example.benomad.service.SuperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SuperServiceImpl implements SuperService {

    private final UserServiceImpl userService;

    @Override
    public MessageResponse makeAdmin(Long userId) {
        User user = userService.getUserEntityById(userId);
        user.addRole(Role.ROLE_ADMIN);
        return new MessageResponse(String.format("Administrative privileges has been successfully given to user - %s",
                user.getEmail()), 200);
    }

    @Override
    public MessageResponse removeAdmin(Long userId) {
        User user = userService.getUserEntityById(userId);
        user.removeRole(Role.ROLE_ADMIN);
        return new MessageResponse(String.format("Administrative privileges has been successfully taken from user - %s",
                user.getEmail()), 200);
    }
}
