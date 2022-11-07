package com.example.benomad.mapper;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserMapper {
    public User dtoToEntity(UserDTO userDTO){
        return User.builder()
                .id(userDTO.getId())
                .login(userDTO.getLogin())
                .password(userDTO.getPassword())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .build();
    }

    public UserDTO entityToDto(User user){
        return UserDTO.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public List<UserDTO> entityListToDtoList(List<User> entities){
        List<UserDTO> dtos = new ArrayList<>();
        for(User u : entities){
            u.setPlaces(null);
            dtos.add(entityToDto(u));
        }
        return dtos;
    }

    public List<User> dtoListToEntityList(List<UserDTO> dtos){
        List<User> entities = new ArrayList<>();
        for(UserDTO u : dtos){
            entities.add(dtoToEntity(u));
        }
        return entities;
    }

}
