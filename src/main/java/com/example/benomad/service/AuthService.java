package com.example.benomad.service;

import com.example.benomad.dto.MessageResponse;
import com.example.benomad.dto.UserDTO;
import com.example.benomad.security.request.LoginRequest;
import com.example.benomad.security.request.TokenRefreshRequest;
import com.example.benomad.security.response.JwtResponse;
import com.example.benomad.security.response.TokenRefreshResponse;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    TokenRefreshResponse refreshToken(TokenRefreshRequest request);
    MessageResponse logoutUser(Long id);
    String getCurrentUsername();
    Long getCurrentUserId();
}
