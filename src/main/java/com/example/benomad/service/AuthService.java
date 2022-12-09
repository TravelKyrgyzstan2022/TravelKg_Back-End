package com.example.benomad.service;

import com.example.benomad.dto.MessageResponse;
import com.example.benomad.security.request.*;
import com.example.benomad.security.response.JwtResponse;
import com.example.benomad.security.response.TokenRefreshResponse;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    TokenRefreshResponse refreshToken(TokenRefreshRequest request);
    MessageResponse validateVerificationCode(EmailVerificationRequest request);
    MessageResponse activateUser(EmailVerificationRequest request);
    MessageResponse sendForgotPasswordCode(CodeRequest request);
    MessageResponse sendActivationCode(CodeRequest request);
    MessageResponse logoutUser(Long id);
    MessageResponse resetPassword(ResetPasswordRequest request);
    Long getCurrentUserId();
}
