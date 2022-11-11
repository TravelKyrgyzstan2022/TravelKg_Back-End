package com.example.benomad.service.impl;

import com.example.benomad.dto.MessageResponse;
import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.RefreshToken;
import com.example.benomad.entity.User;
import com.example.benomad.exception.RefreshTokenException;
import com.example.benomad.security.domain.UserDetailsImpl;
import com.example.benomad.security.jwt.JwtUtils;
import com.example.benomad.security.request.LoginRequest;
import com.example.benomad.security.request.TokenRefreshRequest;
import com.example.benomad.security.response.JwtResponse;
import com.example.benomad.security.response.TokenRefreshResponse;
import com.example.benomad.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String email = ((UserDetailsImpl) authentication.getPrincipal()).getEmail();

        String jwt = jwtUtils.generateTokenFromEmail(email);

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String role = roles.contains("ROLE_ADMIN") ? "ADMIN" : "USER";

        RefreshToken refreshToken = refreshTokenService.createToken(userDetails.getId());

        return JwtResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken.getToken())
                .id(userDetails.getId())
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())

                .phoneNumber(userDetails.getPhoneNumber())
                .email(userDetails.getEmail())

                .role(role)
                .build();
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
    public MessageResponse logoutUser(Long id) {
        UserDTO userDTO = userService.getUserById(id);
        refreshTokenService.deleteByUserId(userDTO.getId());
        return new MessageResponse("User has been successfully logged out!", 200);
    }

    @Override
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return null;
    }

    @Override
    public Long getCurrentUserId() {
//        String username = getCurrentUsername();
//        if(username == null){
//            return null;
//        }else{
//            return userService.getUserByEmail(username).getId();
//        }
        return null;
    }

}