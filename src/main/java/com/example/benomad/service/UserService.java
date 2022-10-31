package com.example.benomad.service;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.exception.UserNotFoundException;

import java.util.List;

public interface UserService {
    UserDTO insertUser(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id) throws UserNotFoundException;
    UserDTO getUserByLogin(String login) throws UserNotFoundException;
    List<UserDTO> getUsersByFirstName(String firstName) throws UserNotFoundException;
    List<UserDTO> getUsersByLastName(String lastName) throws UserNotFoundException;
    List<UserDTO> getUsersByFirstNameAndLastName(String firstName, String lastName) throws UserNotFoundException;
    UserDTO getUserByEmail(String email) throws UserNotFoundException;
    UserDTO getUserByPhoneNumber(String phoneNumber) throws UserNotFoundException;
    UserDTO updateUserById(Long id, UserDTO userDTO) throws UserNotFoundException;
    UserDTO deleteUserById(Long id) throws UserNotFoundException;
}
