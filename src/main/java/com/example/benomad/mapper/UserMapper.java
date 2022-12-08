package com.example.benomad.mapper;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.User;
import com.example.benomad.security.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final DeletionInfoMapper deletionInfoMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' HH:mm");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public User dtoToEntity(UserDTO userDTO){
        return User.builder()
                .id(userDTO.getId())
                .password(userDTO.getPassword())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .isDeleted(userDTO.getDeleted())
                .isActivated(userDTO.getActivated())
                .deletionInfo(userDTO.getDeletionInfoDTO() != null ?
                        deletionInfoMapper.dtoToEntity(userDTO.getDeletionInfoDTO()) : null)
                .roles(userDTO.getRoles())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .imageUrl(userDTO.getImageUrl())
                .build();
    }

    public UserDTO entityToDto(User user){
        String role;
        Set<Role> roles = user.getRoles();
        if(roles.contains(Role.ROLE_SUPERADMIN)){
            role = Role.ROLE_SUPERADMIN.toString();
        }else if(roles.contains(Role.ROLE_ADMIN)){
            role = Role.ROLE_ADMIN.toString();
        }else{
            role = Role.ROLE_USER.toString();
        }
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .activated(user.getIsActivated())
                .deleted(user.getIsDeleted())
                .lastVisitDate(user.getLastVisitDate() != null ?
                        formatter.format(user.getLastVisitDate()) : "Haven't visited yet")
                .registrationDate(user.getRegistrationDate() != null ?
                        dateFormatter.format(user.getRegistrationDate()) : null)
                .deletionInfoDTO(user.getDeletionInfo() != null ?
                        deletionInfoMapper.entityToDto(user.getDeletionInfo()) : null)
                .phoneNumber(user.getPhoneNumber())
                .role(role)
                .roles(user.getRoles())
                .imageUrl(user.getImageUrl().orElse(null))
                .build();
    }

    public List<UserDTO> entityListToDtoList(List<User> entities){
        return entities.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public List<User> dtoListToEntity(List<UserDTO> dtos){
        return dtos.stream().map(this::dtoToEntity).collect(Collectors.toList());
    }

}
