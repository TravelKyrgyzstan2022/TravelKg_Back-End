package com.example.benomad.service.impl;

import com.example.benomad.dto.MessageResponse;
import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.RefreshToken;
import com.example.benomad.entity.User;
import com.example.benomad.exception.RefreshTokenException;
import com.example.benomad.exception.UserAlreadyActivatedException;
import com.example.benomad.exception.UserAttributeTakenException;
import com.example.benomad.exception.UserNotActivatedException;
import com.example.benomad.mapper.UserMapper;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.security.domain.Claims;
import com.example.benomad.security.domain.Role;
import com.example.benomad.security.domain.UserDetailsImpl;
import com.example.benomad.security.request.*;
import com.example.benomad.util.CodeGenerator;
import com.example.benomad.util.JwtUtils;
import com.example.benomad.security.response.JwtResponse;
import com.example.benomad.security.response.TokenRefreshResponse;
import com.example.benomad.service.AuthService;
import com.example.benomad.util.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserServiceImpl userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final VerificationCodeServiceImpl codeService;
    private final AuthenticationManager authenticationManager;
    private final EmailSender emailSender;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final JwtUtils jwtUtils;

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String email = ((UserDetailsImpl) authentication.getPrincipal()).getEmail();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        Role role;
        String jwt;

        if (roles.contains("ROLE_SUPERADMIN")) {
            role = Role.ROLE_SUPERADMIN;
            jwt = jwtUtils.generateAdminTokenFromEmail(email);
        } else if (roles.contains("ROLE_ADMIN")) {
            role = Role.ROLE_ADMIN;
            jwt = jwtUtils.generateAdminTokenFromEmail(email);
        } else {
            role = Role.ROLE_USER;
            jwt = jwtUtils.generateTokenFromEmail(email);
        }

        UserDTO userDTO = userService.getUserById(userDetails.getId());
        User user = userService.getUserEntityById(userDetails.getId());
        if (!user.getIsActivated()) {
            throw new UserNotActivatedException();
        }
        user.setLastVisitDate(LocalDateTime.now(ZoneId.of("Asia/Bishkek")));
        userRepository.save(user);
        RefreshToken refreshToken = refreshTokenService.createToken(userDetails.getId());
        return JwtResponse.builder()
                .role(role)
                .token(jwt)
                .refreshToken(refreshToken.getToken())
                .userDTO(userDTO)
                .build();
    }

    @Override
    public MessageResponse validateVerificationCode(EmailVerificationRequest request) {
        String email = request.getEmail();
        String code = request.getVerificationCode();
        codeService.isCodeValid(email, code);
        return new MessageResponse("Verification code has been successfully validated", 200);
    }

    @Override
    public MessageResponse sendActivationCode(CodeRequest codeRequest) {
        String email = codeRequest.getEmail();
        if (userService.getUserEntityByEmail(email).getIsActivated()) {
            throw new UserAlreadyActivatedException();
        }
        String code = CodeGenerator.generateActivationCode();
        codeService.newCode(email, code);
        emailSender.sendActivationCode(email, code);
        return new MessageResponse("Activation code has been sent to email - " + email,
                200);
    }

    @Override
    public UserDTO registerUser(RegistrationRequest request) {

        UserDTO userDTO = UserDTO.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
        User user = userMapper.dtoToEntity(userDTO);

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserAttributeTakenException("email: ('" + user.getEmail() + "')");
        }

        user.setId(null);
        user.setIsActivated(false);
        user.setIsDeleted(false);
        user.setRegistrationDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        user.setLastVisitDate(LocalDateTime.now(ZoneId.of("Asia/Bishkek")));
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setPassword(encoder.encode(user.getPassword()));

        userRepository.save(user);
        sendActivationCode(new CodeRequest(userDTO.getEmail()));
        return userMapper.entityToDto(user);
    }

    @Override
    public MessageResponse activateUser(EmailVerificationRequest request) {
        String email = request.getEmail();
        String code = request.getVerificationCode();
        if (userService.getUserEntityByEmail(email).getIsActivated()) {
            throw new UserAlreadyActivatedException();
        }
        if (codeService.isCodeValid(email, code)) {
            userService.setActivated(email);
            codeService.deleteCode(email, code);
        }
        return new MessageResponse("User has been successfully activated", 200);
    }

    @Override
    public MessageResponse sendForgotPasswordCode(CodeRequest codeRequest) {
        String email = codeRequest.getEmail();
        userService.getUserByEmail(email);
        String code = CodeGenerator.generateResetPasswordCode();
        codeService.newCode(email, code);
        emailSender.sendForgotPasswordCode(email, code);
        return new MessageResponse("Verification code has been sent to email - " + email, 200);
    }

    @Override
    public MessageResponse resetPassword(ResetPasswordRequest request) {
        String email = request.getEmail();
        String newPassword = request.getNewPassword();
        String code = request.getVerificationCode();
        if (codeService.isCodeValid(email, code)) {
            codeService.deleteCode(email, code);
            userService.resetPassword(email, newPassword);
        }
        return new MessageResponse("Password has been reset for email - " + email, 200);
    }

    @Override
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {

        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = userService.getUserAuthenticationToken(user);
                    return TokenRefreshResponse.builder()
                            .accessToken(token)
                            .refreshToken(requestRefreshToken)
                            .build();
                })
                .orElseThrow(() -> new RefreshTokenException(requestRefreshToken, "Refresh token is not in the database!"));
    }

    @Override
    public MessageResponse logoutUser() {
        refreshTokenService.deleteByUserId(getCurrentUserId());
        return new MessageResponse("User has been successfully logged out!", 200);
    }

    @Override
    public Long getCurrentUserId() {
        String username = getCurrentEmail();
        if (username == null) {
            return null;
        } else {
            return userService.getUserByEmail(username).getId();
        }
    }

    @Override
    public String getCurrentEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return null;
    }
}
