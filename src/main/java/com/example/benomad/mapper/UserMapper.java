package com.example.benomad.mapper;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.User;

public class UserMapper {

    public static User userDtoToUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());

        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRoles(userDTO.getRoles());

//        user.setResetPasswordCode(userDTO.getResetPasswordCode());
//        user.setCodeExpirationDate(userDTO.getCodeExpirationDate());
//        user.setPwdChangeRequired(userDTO.isPwdChangeRequired());

        return user;
    }

    public static UserDTO userToUserDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());

        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRoles(user.getRoles());

//        userDTO.setResetPasswordCode(user.getResetPasswordCode());
//        userDTO.setCodeExpirationDate(user.getCodeExpirationDate());
//        userDTO.setPwdChangeRequired(user.isPwdChangeRequired());

        return userDTO;
    }
}
