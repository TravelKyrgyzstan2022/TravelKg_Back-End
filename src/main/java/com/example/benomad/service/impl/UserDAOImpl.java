package com.example.benomad.service.impl;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.mapper.UserMapper;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.dao.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDAOImpl implements UserDAO {

    private final UserRepository userRepository;

    @Override
    public UserDTO getUserById(Long id) throws ContentNotFoundException {
        return UserMapper.entityToDto(userRepository.findById(id).orElseThrow(
                ContentNotFoundException::new
        ));
    }
}
