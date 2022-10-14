package com.example.benomad.service.dao;

import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.dto.UserDTO;
import com.example.benomad.exception.ContentNotFoundException;

import java.util.List;

public interface UserDAO {
    public UserDTO getUserById(Long id) throws ContentNotFoundException;
}
