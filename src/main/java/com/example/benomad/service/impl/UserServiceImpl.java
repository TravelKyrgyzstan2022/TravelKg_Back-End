package com.example.benomad.service.impl;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.User;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.mapper.UserMapper;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> entities = userRepository.findAll();
        List<UserDTO> dtos = new ArrayList<>();
        for(User u : entities){
            dtos.add(UserMapper.entityToDto(u));
        }
        return dtos;
    }

    @Override
    public UserDTO insertUser(UserDTO userDTO) {
        userRepository.save(UserMapper.dtoToEntity(userDTO));
        userDTO.setId(userRepository.getLastValueOfArticleSequence());
        return userDTO;
    }

    @Override
    public UserDTO updateUserById(Long id, UserDTO userDTO) throws ContentNotFoundException {
        userRepository.findById(id).orElseThrow(
                ContentNotFoundException::new);
        userDTO.setId(id);
        userRepository.save(UserMapper.dtoToEntity(userDTO));
        return userDTO;
    }

    @Override
    public UserDTO deleteUserById(Long id) throws ContentNotFoundException {
        User user = userRepository.findById(id).orElseThrow(
                ContentNotFoundException::new);
        return UserMapper.entityToDto(user);
    }


}
