package com.example.benomad.service;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.exception.ContentNotFoundException;

import java.util.List;

public interface UserService {
    UserDTO addUser(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id) throws ContentNotFoundException;
    UserDTO getUserByLogin(String login) throws ContentNotFoundException;
    List<UserDTO> getUsersByFirstName(String firstName) throws ContentNotFoundException;
    List<UserDTO> getUsersByLastName(String lastName) throws ContentNotFoundException;
    List<UserDTO> getUsersByFirstNameAndLastName(String firstName, String lastName) throws ContentNotFoundException;
    UserDTO getUserByEmail(String email) throws ContentNotFoundException;
    UserDTO getUserByPhoneNumber(String phoneNumber) throws ContentNotFoundException;
    UserDTO updateUserById(Long id, UserDTO userDTO) throws ContentNotFoundException;
    UserDTO deleteUserById(Long id) throws ContentNotFoundException;
}
