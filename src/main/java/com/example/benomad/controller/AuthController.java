package com.example.benomad.controller;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.User;
import com.example.benomad.security.request.LogOutRequest;
import com.example.benomad.security.request.LoginRequest;
import com.example.benomad.security.request.TokenRefreshRequest;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.service.impl.AuthServiceImpl;
import com.example.benomad.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth/")
@Validated
@RequiredArgsConstructor
@Tag(name = "Authorization Resource", description = "The Auth API")
public class AuthController {

    private final AuthServiceImpl authService;
    private final UserServiceImpl userService;

    @Operation(summary = "Signs in the user",
    description = """
            Signs in the user to the system and returns user's details with new generated JWT and refresh token.
        

            In order to authorize requests - JWT must be added to request header with name 'Authorization'.
            JWT expiration time - 15 mins.
            Refresh token expiration time - 14 days.""")
    @PostMapping(value = "/signin", produces = "application/json")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }

    @Operation(summary = "Gets JWT via using refresh token",
    description = """
            Gets new JWT token for user to make authorized requests.""")
    @PostMapping(value = "/refreshtoken", produces = "application/json")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @Operation(summary = "Logs out the user from system",
    description = """
            Just deletes user's refreshtoken from database so user won't be able to sign-in with it.
            
            The access token must be deleted from client side, because it will still be valid until it expires.""")
    @PostMapping(value = "/logout", produces = "application/json")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
        return ResponseEntity.ok(authService.logoutUser(logOutRequest.getUserId()));
    }

    @Operation(summary = "Registers user to the system",
    description = """
            Adds user to the database with given attributes.

            Note:This operation doesn't sign-in user, meaning that after registration user will need to sign-in in order to get access token.""")
    @PostMapping("/register")
    public ResponseEntity<?> addUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.addUser(userDTO));
    }

    //fixme
//    @PostMapping("/activate/{code}")
//    public ResponseEntity<?> activate(@PathVariable String code) {
//        boolean isActivated = userService.activateUser(code);
//
//        if (isActivated) {
//            return ResponseEntity.ok(new MessageResponse("Account has been successfully activated!", 200));
//        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(
//                "Activation code is not valid.", 400
//        ));
//    }
}
