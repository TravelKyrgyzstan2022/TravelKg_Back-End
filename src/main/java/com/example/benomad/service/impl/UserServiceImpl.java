package com.example.benomad.service.impl;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.mapper.UserMapper;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO getUserById(Long id) throws ContentNotFoundException {
        return UserMapper.entityToDto(userRepository.findById(id).orElseThrow(
                ContentNotFoundException::new
        ));
    }
}
