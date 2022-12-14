package com.example.benomad.controller;

import com.example.benomad.advice.ExceptionResponse;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.dto.UserDTO;
import com.example.benomad.security.request.*;
import com.example.benomad.security.response.JwtResponse;
import com.example.benomad.security.response.TokenRefreshResponse;
import com.example.benomad.service.CommentService;
import com.example.benomad.service.impl.AuthServiceImpl;
import com.example.benomad.service.impl.BlogServiceImpl;
import com.example.benomad.service.impl.CommentServiceImpl;
import com.example.benomad.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/account/")
@Validated
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = "Account Resource", description = "The Account API")
public class AccountController {

    private final AuthServiceImpl authService;
    private final UserServiceImpl userService;

    @Operation(summary = "Signs in the user",
    description = """
            Signs in the user to the system and returns user's details with new generated JWT and refresh token.
        

            In order to authorize requests - JWT must be added to request header with name 'Authorization'.
            JWT expiration time - 15 mins.
            Refresh token expiration time - 14 days.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))
            ),
            @ApiResponse(
                    responseCode = "Any error",
                    description = "Every response starting with 4** or 5** will have this body",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            )
    })
    @PostMapping(value = "/sign-in", produces = "application/json")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }

    @Operation(summary = "Gets JWT via using refresh token",
    description = """
            Gets new JWT token for user to make authorized requests.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = TokenRefreshResponse.class))
            ),
            @ApiResponse(
                    responseCode = "Any error",
                    description = "Every response starting with 4** or 5** will have this body",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            )
    })
    @PostMapping(value = "/refresh-token", produces = "application/json")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @Operation(summary = "Logs out the user from system",
    description = """
            Just deletes user's refreshtoken from database so user won't be able to sign-in with it.
            
            The access token must be deleted from client side, because it will still be valid until it expires.""")
    @PostMapping(value = "/logout", produces = "application/json")
    public ResponseEntity<?> logoutUser() {
        return ResponseEntity.ok(authService.logoutUser());
    }

    @Operation(summary = "Registers user to the system and sends email verification code",
    description = """
            Adds user to the database with given attributes.

            Note:This operation doesn't sign-in user, meaning that after registration user will need to sign-in in order to get access token.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(
                    responseCode = "Any error",
                    description = "Every response starting with 4** or 5** will have this body",
                    content = @Content(schema = @Schema(implementation = ExceptionResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict (email already taken)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            )
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(authService.registerUser(request));
    }

    @Operation(summary = "Sends 'forgot password' verification code to the given email")
    @PostMapping(value = "/forgot-password", produces = "application/json")
    public ResponseEntity<MessageResponse> sendVerificationCode(@Valid @RequestBody CodeRequest codeRequest){
        return ResponseEntity.ok(authService.sendForgotPasswordCode(codeRequest));
    }

    @Operation(summary = "Validates both code and email (idk why did I add this)")
    @PostMapping(value = {"/validate-code", "/verify-email"}, produces = "application/json")
    public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody EmailVerificationRequest emailVerificationRequest){
        return ResponseEntity.ok(authService.validateVerificationCode(emailVerificationRequest));
    }

    @Operation(summary = "Resets the password for given email")
    @PutMapping(value = "/reset-password", produces = "application/json")
    public ResponseEntity<MessageResponse> setNewPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest){
        return ResponseEntity.ok(authService.resetPassword(resetPasswordRequest));
    }

    @Operation(summary = "Sends activation code to email")
    @PostMapping(value = "/send-activation-code", produces = "application/json")
    public ResponseEntity<MessageResponse> sendActivationCode(@Valid @RequestBody CodeRequest codeRequest){
        return ResponseEntity.ok(authService.sendActivationCode(codeRequest));
    }

    @Operation(summary = "Activates current account")
    @PostMapping(value = "/activate", produces = "application/json")
    public ResponseEntity<MessageResponse> activateUser(@Valid @RequestBody EmailVerificationRequest emailVerificationRequest){
        return ResponseEntity.ok(authService.activateUser(emailVerificationRequest));
    }
}
