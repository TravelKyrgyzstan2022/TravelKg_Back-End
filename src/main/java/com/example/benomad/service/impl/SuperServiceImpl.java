package com.example.benomad.service.impl;

import com.example.benomad.dto.MessageResponse;
import com.example.benomad.entity.User;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.security.domain.Role;
import com.example.benomad.service.SuperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
@RequiredArgsConstructor
public class SuperServiceImpl implements SuperService {

    private final UserServiceImpl userService;
    private final UserRepository userRepository;

    @Override
    public MessageResponse makeAdmin(Long userId) {
        User user = userService.getUserEntityById(userId);
        Set<Role> roles = user.getRoles();
        if(!roles.contains(Role.ROLE_ADMIN)){
            roles.add(Role.ROLE_ADMIN);
            user.setRoles(roles);
            userRepository.save(user);
            return new MessageResponse(String.format("Administrative privileges has already been given to user - %s",
                    user.getEmail()), 200);
        }
        return new MessageResponse(String.format("Administrative privileges has been successfully given to user - %s",
                user.getEmail()), 200);
    }

    @Override
    public MessageResponse removeAdmin(Long userId) {
        User user = userService.getUserEntityById(userId);
        Set<Role> roles = user.getRoles();
        if(roles.contains(Role.ROLE_ADMIN)){
            roles.remove(Role.ROLE_ADMIN);
            user.setRoles(roles);
            userRepository.save(user);
            return new MessageResponse(String.format("Administrative privileges has been successfully taken from user - %s",
                    user.getEmail()), 200);
        }
        return new MessageResponse(String.format("Administrative privileges has been already taken from user - %s",
                user.getEmail()), 200);
    }
}
