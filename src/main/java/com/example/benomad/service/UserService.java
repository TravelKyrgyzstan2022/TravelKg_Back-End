package com.example.benomad.service;

import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.dto.UserDTO;
import com.example.benomad.exception.ContentNotFoundException;

import java.util.List;

public interface UserService {
    public UserDTO addUser(UserDTO userDTO);
    public List<UserDTO> getAllUsers();
    public UserDTO getUserById(Long id) throws ContentNotFoundException;
    public UserDTO getUserByLogin(String login) throws ContentNotFoundException;
    public List<UserDTO> getUsersByFirstName(String firstName) throws ContentNotFoundException;
    public List<UserDTO> getUsersByLastName(String lastName) throws ContentNotFoundException;
    public List<UserDTO> getUsersByFirstNameAndLastName(String firstName, String lastName) throws ContentNotFoundException;
    public UserDTO getUserByEmail(String email) throws ContentNotFoundException;
    public UserDTO getUserByPhoneNumber(String phoneNumber) throws ContentNotFoundException;
    public UserDTO updateUserById(Long id, UserDTO userDTO) throws ContentNotFoundException;
    public UserDTO deleteUserById(Long id) throws ContentNotFoundException;
}
