package com.example.benomad.mapper;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.User;

public class UserMapper {
    public static User dtoToEntity (UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .build();
    }
    public static UserDTO entityToDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .build();
    }
}
