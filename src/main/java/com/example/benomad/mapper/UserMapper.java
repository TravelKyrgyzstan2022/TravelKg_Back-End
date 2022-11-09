package com.example.benomad.mapper;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static User dtoToEntity(UserDTO userDTO){
        return User.builder()
                .id(userDTO.getId())
                .login(userDTO.getLogin())
                .password(userDTO.getPassword())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .roles(userDTO.getRoles())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .build();
    }

    public static UserDTO entityToDto(User user){
        return UserDTO.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .roles(user.getRoles())
                .build();
    }

    public List<UserDTO> entityListToDtoList(List<User> entities){
        return entities.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public List<User> entityListToDtoList(List<UserDTO> dtos){
        return dtos.stream().map(this::dtoToEntity).collect(Collectors.toList());
    }

}
