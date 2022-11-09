package com.example.benomad.controller;


import com.example.benomad.exception.NotFoundException;
import com.example.benomad.exception.RefreshTokenException;
import com.example.benomad.security.request.LogOutRequest;
import com.example.benomad.security.request.LoginRequest;
import com.example.benomad.security.request.TokenRefreshRequest;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth/")
@Validated
@RequiredArgsConstructor
//@Tag(name = "Auth Controller", description = "The Auth API with documentation annotations")
public class AuthController {

    private final AuthServiceImpl authService;


    @PostMapping(value = "/signin", produces = "application/json")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }
    
    @PostMapping(value = "/refreshtoken", produces = "application/json")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        try{
            return ResponseEntity.ok(authService.refreshToken(request));
        } catch (RefreshTokenException ex) {
            throw new RefreshTokenException(request.getRefreshToken(), ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
    }

    @PostMapping(value = "/logout", produces = "application/json")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
        try{
            authService.logoutUser(logOutRequest.getUserId());
            return ResponseEntity.ok(new MessageResponse("Log out successful!"));
        } catch (NotFoundException ex) {
            throw new NotFoundException(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
        }
    }
}
