package com.example.benomad.mapper;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.User;

public class UserMapper {
    public static User userDtoToUser (UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .build();
    }
    public static UserDTO userToUserDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .build();
    }
}
