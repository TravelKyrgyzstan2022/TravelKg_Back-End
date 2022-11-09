package com.example.benomad.service;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.exception.UserAttributeTakenException;
import com.example.benomad.exception.ContentNotFoundException;

import java.util.List;

public interface UserService {
    UserDTO insertUser(UserDTO userDTO) throws UserAttributeTakenException;
    List<UserDTO> getUsersByAttributes(String login, String firstName, String lastName, String email,
                                       String phoneNumber, boolean MATCH_ALL);
    UserDTO getUserById(Long id) throws ContentNotFoundException;
    UserDTO updateUserById(Long id, UserDTO userDTO) throws ContentNotFoundException;
    UserDTO deleteUserById(Long id) throws ContentNotFoundException;
}
