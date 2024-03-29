package com.example.benomad.service;

import com.example.benomad.dto.DeletionInfoDTO;
import com.example.benomad.dto.ImageDTO;
import com.example.benomad.dto.UserDTO;

import com.example.benomad.enums.IncludeContent;
import com.example.benomad.exception.UserAttributeTakenException;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.entity.User;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.security.domain.UserDetailsImpl;
import com.example.benomad.security.response.JwtResponse;
import com.example.benomad.security.response.TokenRefreshResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface UserService {
    String getUserAuthenticationToken(User user);
    UserDTO insertUser(UserDTO userDTO);
    void setActivated(String email);
    void resetPassword(String email, String password);
    UserDTO getUserById(Long id);
    List<UserDTO> getUsersByAttributes(String firstName, String lastName, IncludeContent includeContent,
                                       String email, String phoneNumber, boolean MATCH_ALL);
    List<UserDTO> getBlogAuthors(String firstName, String lastName);
    UserDTO getUserByEmail(String email);
    UserDTO getCurrentUser();
    UserDTO updateCurrentUser(UserDTO userDTO);
    UserDTO updateUserById(Long id, UserDTO userDTO);
    UserDTO deleteUserById(Long userId, DeletionInfoDTO infoDTO);
    MessageResponse insertMyImage(MultipartFile file);
    User getUserEntityById(Long userId);
    User getUserEntityByEmail(String email);

    MessageResponse insertMyImage64(ImageDTO file);
}
