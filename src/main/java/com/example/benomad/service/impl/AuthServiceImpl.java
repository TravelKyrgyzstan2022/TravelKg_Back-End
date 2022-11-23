package com.example.benomad.service.impl;

import com.example.benomad.dto.MessageResponse;
import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.RefreshToken;
import com.example.benomad.exception.RefreshTokenException;
import com.example.benomad.exception.UserAlreadyActivatedException;
import com.example.benomad.logger.LogWriterServiceImpl;
import com.example.benomad.security.domain.Claims;
import com.example.benomad.security.domain.UserDetailsImpl;
import com.example.benomad.security.request.EmailVerificationRequest;
import com.example.benomad.security.request.ResetPasswordRequest;
import com.example.benomad.util.CodeGenerator;
import com.example.benomad.util.JwtUtils;
import com.example.benomad.security.request.LoginRequest;
import com.example.benomad.security.request.TokenRefreshRequest;
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
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserServiceImpl userService;
    private final VerificationCodeServiceImpl codeService;
    private final AuthenticationManager authenticationManager;
    private final EmailSender emailSender;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final JwtUtils jwtUtils;
    private final LogWriterServiceImpl logWriter;

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String email = ((UserDetailsImpl) authentication.getPrincipal()).getEmail();

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        String role;
        String jwt;
        Claims claims = new Claims();

        if(roles.contains("ROLE_SUPERADMIN")){
            role = "SUPERADMIN";
            jwt = jwtUtils.generateAdminTokenFromEmail(email);
//            claims = getClaims(Role.ROLE_SUPERADMIN);
        }else if(roles.contains("ROLE_ADMIN")){
            role = "ADMIN";
            jwt = jwtUtils.generateAdminTokenFromEmail(email);
//            claims = getClaims(Role.ROLE_ADMIN);
        }else{
            role = "USER";
            jwt = jwtUtils.generateTokenFromEmail(email);
//            claims = getClaims(Role.ROLE_USER);
        }
        UserDTO userDTO = userService.getUserById(userDetails.getId());
        RefreshToken refreshToken = refreshTokenService.createToken(userDetails.getId());
        logWriter.auth(String.format("%s - Authenticated", loginRequest.getEmail()));
        return JwtResponse.builder()
                .claims(claims)
                .token(jwt)
                .refreshToken(refreshToken.getToken())
                .userDTO(userDTO)
                .build();
    }

    @Override
    public MessageResponse validateVerificationCode(EmailVerificationRequest request){
        String email = request.getEmail();
        String code = request.getVerificationCode();
        codeService.isCodeValid(email, code);
        return new MessageResponse("Verification code has been successfully validated", 200);
    }

    @Override
    //fixme maybe need to move code related to mail sending to verification code service
    public MessageResponse sendActivationCode() {
        if(userService.getUserEntityByEmail(getCurrentEmail()).isActivated()){
            throw new UserAlreadyActivatedException();
        }
        String code = CodeGenerator.generateActivationCode();
        codeService.newCode(getCurrentEmail(), code);
        String mailMessage = String.format("""
                Your activation code for Benomad account is <%s>.

                Expiration time - 10 minutes.""", code);
        emailSender.send(getCurrentEmail(), "Activation code", mailMessage);
        return new MessageResponse("Activation code has been sent to email - " + getCurrentEmail(),
                200);
    }

    @Override
    public MessageResponse activateUser(String code) {
        String email = getCurrentEmail();
        if(userService.getUserEntityByEmail(email).isActivated()){
            throw new UserAlreadyActivatedException();
        }
        if(codeService.isCodeValid(email, code)){
            userService.setActivated(email);
            codeService.deleteCode(email, code);
        }
        return new MessageResponse("User has been successfully activated", 200);
    }

    @Override
    //fixme
    public MessageResponse sendForgotPasswordCode(String email) {
        userService.getUserByEmail(email);

        String code = CodeGenerator.generateResetPasswordCode();
        codeService.newCode(email, code);
        String mailMessage = String.format("""
                Your email verification code for Benomad account:
                
                %s

                Expiration time - 10 minutes.""", code);
        emailSender.send(email, "Reset password: email verification", mailMessage);
        return new MessageResponse("Verification code has been sent to email - " + email, 200);
    }

    @Override
    public MessageResponse resetPassword(ResetPasswordRequest request) {
        String email = request.getEmail();
        String newPassword = request.getNewPassword();
        String code = request.getVerificationCode();
        if(codeService.isCodeValid(email, code)){
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
    public MessageResponse logoutUser(Long id) {
        UserDTO userDTO = userService.getUserById(id);
        refreshTokenService.deleteByUserId(userDTO.getId());
        logWriter.auth(String.format("%s - Logged out", userDTO.getEmail()));
        return new MessageResponse("User has been successfully logged out!", 200);
    }

    @Override
    public Long getCurrentUserId() {
        String username = getCurrentEmail();
        if(username == null){
            return null;
        }else{
            return userService.getUserByEmail(username).getId();
        }
    }

    public String getCurrentEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return null;
    }

//    private Claims getClaims(Role role){
//        String[] superAdminGetClaims = {
//                "**"
//        };
//        String[] superAdminPostClaims = {
//                "**"
//        };
//        String[] superAdminPutClaims = {
//                "**"
//        };
//        String[] superAdminDeleteClaims = {
//                "**"
//        };
//        String[] adminGetClaims = {
//                "/actuator/**",
//                "/api/dev/**",
//                "/api/v1/users",
//                "/api/v1/users/**"
//        };
//        String[] userClaims = {
//                "/api/auth/**",
//                "/documentation/**",
//                "/v3/api-docs/**",
//                "/swagger-ui/**",
//                "/v3/api-docs.yaml",
//                "/error"
//        };
//        String[] guestClaims = {
//                "/api/auth/**",
//                "/documentation/**",
//                "/v3/api-docs/**",
//                "/swagger-ui/**",
//                "/v3/api-docs.yaml",
//                "/error"
//        };
//        List<String> userClaimsList = Arrays.stream(userClaims).toList();
//
//        List<String> adminClaimsList = Arrays.stream(adminGetClaims).toList();
//        adminClaimsList.addAll(userClaimsList);
//
//        List<String> superAdminClaimsList = Arrays.stream(superAdminClaims).toList();
//
//        Claims claims = new Claims();
//
//        if(role == Role.ROLE_SUPERADMIN){
//             claims.setGet(Arrays.stream(superAdminClaims).toList());
//             claims.setPost(Arrays.stream(superAdminPostClaims).toList());
//        }
//        if(role == Role.ROLE_ADMIN){
//            return Arrays.stream(adminGetClaims).toList();
//        }else{
//            return Arrays.stream(userClaims).toList();
//        }
//        return claims;
//    }
}