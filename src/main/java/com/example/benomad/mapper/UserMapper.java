package com.example.benomad.mapper;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final DeletionInfoMapper deletionInfoMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' hh:mm");

    public User dtoToEntity(UserDTO userDTO){
        return User.builder()
                .id(userDTO.getId())
                .password(userDTO.getPassword())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .isDeleted(userDTO.isDeleted())
                .isActivated(userDTO.isActivated())
                .deletionInfo(userDTO.getDeletionInfoDTO() != null ?
                        deletionInfoMapper.dtoToEntity(userDTO.getDeletionInfoDTO()) : null)
                .roles(userDTO.getRoles())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .build();
    }

    public UserDTO entityToDto(User user){
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .activated(user.isActivated())
                .deleted(user.isDeleted())
                .lastVisitDate(user.getLastVisitDate() != null ?
                        formatter.format(user.getLastVisitDate()) : "Haven't visited yet")
                .registrationDate(user.getRegistrationDate())
                .deletionInfoDTO(user.getDeletionInfo() != null ?
                        deletionInfoMapper.entityToDto(user.getDeletionInfo()) : null)
                .phoneNumber(user.getPhoneNumber())
                .roles(user.getRoles())
                .build();
    }

    public List<UserDTO> entityListToDtoList(List<User> entities){
        return entities.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public List<User> dtoListToEntity(List<UserDTO> dtos){
        return dtos.stream().map(this::dtoToEntity).collect(Collectors.toList());
    }

}
