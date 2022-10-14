package com.example.benomad.service;

import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.dto.UserDTO;
import com.example.benomad.exception.ContentNotFoundException;

import java.util.List;

public interface UserService {
    public UserDTO getUserById(Long id) throws ContentNotFoundException;
}
