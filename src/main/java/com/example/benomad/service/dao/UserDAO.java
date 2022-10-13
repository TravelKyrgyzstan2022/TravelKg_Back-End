package com.example.benomad.service.dao;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.exception.ContentNotFoundException;

public interface UserDAO {
    public UserDTO getUserById(Long id) throws ContentNotFoundException;
}
