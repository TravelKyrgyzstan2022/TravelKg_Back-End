package com.example.benomad.service;

import com.example.benomad.dto.UserDTO;

import com.example.benomad.exception.UserAttributeTakenException;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.entity.User;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.security.domain.UserDetailsImpl;
import com.example.benomad.security.response.JwtResponse;
import com.example.benomad.security.response.TokenRefreshResponse;


public interface UserService {
    String getUserAuthenticationToken(UserDetailsImpl userDetails);
    String getUserAuthenticationToken(User user);
//    boolean isUserExistsByPhoneNumber(String phoneNumber);
//    void isUserExistByEmailAndPhoneNumber(String email, String phoneNumber);
//    List<UserDTO> getAllNotDeletedUsers();
//    List<UserDTO> getAllDeletedUsers();
//    UserDTO getNotDeletedUserById(Long id);
//    UserDTO insert(UserDTO signupRequest) throws MessagingException;
//    UserDTO updateNotDeletedUserById(Long id, UserDTO userDTO);
//    void deleteNotDeletedUserById(Long id);
//    MessageResponse updateResetPasswordCode(String emailOrPhoneNumber)
//            throws MessagingException, UnsupportedEncodingException;
//    UserDTO getByResetPasswordCode(Integer code);
//    JwtResponse verifyResetPasswordCodeExpirationDate(User user);
//    void updatePassword(Long id, String oldPassword, String newPassword);
//    void userRecoveryById(Long id);
//    User isUserDeletedCheck(Long id);
    UserDTO insertUser(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id) throws UserNotFoundException;
    UserDTO getUserByLogin(String login) throws UserNotFoundException;
    List<UserDTO> getUsersByFirstName(String firstName) throws UserNotFoundException;
    List<UserDTO> getUsersByLastName(String lastName) throws UserNotFoundException;
    List<UserDTO> getUsersByFirstNameAndLastName(String firstName, String lastName) throws UserNotFoundException;
    UserDTO getUserByEmail(String email) throws UserNotFoundException;
    UserDTO getUserByPhoneNumber(String phoneNumber) throws UserNotFoundException;
    UserDTO updateUserById(Long id, UserDTO userDTO) throws UserNotFoundException;
    UserDTO deleteUserById(Long id) throws UserNotFoundException;
}
