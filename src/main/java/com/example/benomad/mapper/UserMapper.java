package com.example.benomad.mapper;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.User;

public class UserMapper {
    public static UserDTO userToUserDto(User user){
        return UserDTO.builder()
                .id(user.getId())
                .build();
    }

    public static User userDtoToUser(UserDTO dto){
        return User.builder()
                .id(dto.getId())
                .build();
    }
}
