package com.example.benomad.service.impl;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.User;
import com.example.benomad.exception.UserNotFoundException;
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
    public UserDTO getUserById(Long id) throws UserNotFoundException {
        return UserMapper.entityToDto(userRepository.findById(id).orElseThrow(
                UserNotFoundException::new
        ));
    }

    @Override
    public UserDTO getUserByLogin(String login) throws UserNotFoundException {
        return UserMapper.entityToDto(userRepository.findByLogin(login).orElseThrow(
                UserNotFoundException::new
        ));
    }

    @Override
    public List<UserDTO> getUsersByFirstName(String firstName) throws UserNotFoundException {
        List<User> entities = userRepository.findByFirstName(firstName);
        if(entities.size() == 0){
            throw new UserNotFoundException();
        }else{
            return UserMapper.entityListToDtoList(entities);
        }
    }

    @Override
    public List<UserDTO> getUsersByLastName(String lastName) throws UserNotFoundException {
        List<User> entities = userRepository.findByLastName(lastName);
        if(entities.size() == 0){
            throw new UserNotFoundException();
        }else{
            return UserMapper.entityListToDtoList(entities);
        }
    }

    @Override
    public List<UserDTO> getUsersByFirstNameAndLastName(String firstName, String lastName) throws UserNotFoundException {
        List<User> entities = userRepository.findByFirstNameAndLastName(firstName, lastName);
        if(entities.size() == 0){
            throw new UserNotFoundException();
        }else{
            return UserMapper.entityListToDtoList(entities);
        }
    }

    @Override
    public UserDTO getUserByEmail(String email) throws UserNotFoundException {
        return UserMapper.entityToDto(userRepository.findByEmail(email).orElseThrow(
                UserNotFoundException::new
        ));
    }

    @Override
    public UserDTO getUserByPhoneNumber(String phoneNumber) throws UserNotFoundException {
        return UserMapper.entityToDto(userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                UserNotFoundException::new
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
    public UserDTO updateUserById(Long id, UserDTO userDTO) throws UserNotFoundException {
        userRepository.findById(id).orElseThrow(
                UserNotFoundException::new);
        userDTO.setId(id);
        userRepository.save(UserMapper.dtoToEntity(userDTO));
        return userDTO;
    }

    @Override
    public UserDTO deleteUserById(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(
                UserNotFoundException::new);
        return UserMapper.entityToDto(user);
    }

}
