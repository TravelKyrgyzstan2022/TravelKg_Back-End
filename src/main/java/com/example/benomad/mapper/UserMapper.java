package com.example.benomad.mapper;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.User;

public class UserMapper {
    public static UserDTO entityToDto(User user){
        return UserDTO.builder()
                .id(user.getId())
                .login(user.getLogin())
                .placeDTOS(PlaceMapper.listOfEntitiesToListOfDtos(user.getPlaces()))
                .build();
    }

    public static User dtoToEntity(UserDTO dto){
        return User.builder()
                .id(dto.getId())
                .build();
    }
}
