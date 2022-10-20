package com.example.benomad.service.impl;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.User;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.mapper.UserMapper;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public UserDTO getUserByLogin(String login) throws ContentNotFoundException {
        return UserMapper.entityToDto(userRepository.findByLogin(login).orElseThrow(
                ContentNotFoundException::new
        ));
    }

    @Override
    public List<UserDTO> getUsersByFirstName(String firstName) throws ContentNotFoundException {
        List<User> entities = userRepository.findByFirstName(firstName);
        if(entities.size() == 0){
            throw new ContentNotFoundException();
        }else{
            return UserMapper.entityListToDtoList(entities);
        }
    }

    @Override
    public List<UserDTO> getUsersByLastName(String lastName) throws ContentNotFoundException {
        List<User> entities = userRepository.findByLastName(lastName);
        if(entities.size() == 0){
            throw new ContentNotFoundException();
        }else{
            return UserMapper.entityListToDtoList(entities);
        }
    }

    @Override
    public List<UserDTO> getUsersByFirstNameAndLastName(String firstName, String lastName) throws ContentNotFoundException {
        List<User> entities = userRepository.findByFirstNameAndLastName(firstName, lastName);
        if(entities.size() == 0){
            throw new ContentNotFoundException();
        }else{
            return UserMapper.entityListToDtoList(entities);
        }
    }

    @Override
    public UserDTO getUserByEmail(String email) throws ContentNotFoundException {
        return UserMapper.entityToDto(userRepository.findByEmail(email).orElseThrow(
                ContentNotFoundException::new
        ));
    }

    @Override
    public UserDTO getUserByPhoneNumber(String phoneNumber) throws ContentNotFoundException {
        return UserMapper.entityToDto(userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                ContentNotFoundException::new
        ));
    }

    @Override
    public UserDTO addUser(UserDTO userDTO) {
        return UserMapper.entityToDto(userRepository.save(UserMapper.dtoToEntity(userDTO)));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> entities = userRepository.findAll();
        return UserMapper.entityListToDtoList(entities);
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
