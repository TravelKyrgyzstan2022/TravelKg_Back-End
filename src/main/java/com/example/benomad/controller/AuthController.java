package com.example.benomad.controller;

import com.example.benomad.entity.User;
import com.example.benomad.security.request.LogOutRequest;
import com.example.benomad.security.request.LoginRequest;
import com.example.benomad.security.request.TokenRefreshRequest;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.service.impl.AuthServiceImpl;
import com.example.benomad.service.impl.UserServiceImpl;
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

    @PostMapping(value = "/signin", produces = "application/json")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }
    
    @PostMapping(value = "/refreshtoken", produces = "application/json")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping(value = "/logout", produces = "application/json")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
        return ResponseEntity.ok(authService.logoutUser(logOutRequest.getUserId()));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.addUser(user));
    }

    //fixme
    @PostMapping("/activate/{code}")
    public ResponseEntity<?> activate(@PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            return ResponseEntity.ok(new MessageResponse("Account has been successfully activated!", 200));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(
                "Activation code is not valid.", 400
        ));
    }
}
