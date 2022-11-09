package com.example.benomad.service;

import com.example.benomad.dto.UserDTO;

import com.example.benomad.exception.UserAttributeTakenException;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.entity.User;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.security.domain.UserDetailsImpl;
import com.example.benomad.security.response.JwtResponse;
import com.example.benomad.security.response.TokenRefreshResponse;

import java.util.List;


public interface UserService {
    String getUserAuthenticationToken(UserDetailsImpl userDetails);
    String getUserAuthenticationToken(User user);
    UserDTO insertUser(UserDTO userDTO);
    UserDTO getUserById(Long id) throws ContentNotFoundException;
    List<UserDTO> getUsersByAttributes(String firstName, String lastName,
                                       String email, String phoneNumber, boolean MATCH_ALL);
    UserDTO getUserByEmail(String email) throws ContentNotFoundException;
    UserDTO getUserByPhoneNumber(String phoneNumber) throws ContentNotFoundException;
    UserDTO updateUserById(Long id, UserDTO userDTO) throws ContentNotFoundException;
    UserDTO deleteUserById(Long id) throws ContentNotFoundException;



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
}
