package com.example.benomad.service;

import com.example.benomad.dto.MessageResponse;
import com.example.benomad.security.request.EmailVerificationRequest;
import com.example.benomad.security.request.LoginRequest;
import com.example.benomad.security.request.ResetPasswordRequest;
import com.example.benomad.security.request.TokenRefreshRequest;
import com.example.benomad.security.response.JwtResponse;
import com.example.benomad.security.response.TokenRefreshResponse;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    TokenRefreshResponse refreshToken(TokenRefreshRequest request);
    MessageResponse validateVerificationCode(EmailVerificationRequest request);
    MessageResponse activateUser(EmailVerificationRequest request);
    MessageResponse sendForgotPasswordCode(String email);
    MessageResponse sendActivationCode(String email);
    MessageResponse logoutUser(Long id);
    MessageResponse resetPassword(ResetPasswordRequest request);
    Long getCurrentUserId();
}
