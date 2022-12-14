package com.example.benomad.controller;

import com.example.benomad.advice.ExceptionResponse;
import com.example.benomad.dto.CommentDTO;
import com.example.benomad.dto.DeletionInfoDTO;
import com.example.benomad.dto.UserDTO;
import com.example.benomad.enums.IncludeContent;
import com.example.benomad.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/users")
@Tag(name = "Admin Resource", description = "The Administrator API")
public class AdminUserController {

    private final UserServiceImpl userService;

    @Operation(summary = "Gets all users / Finds users by attributes (ADMIN)",
    description = """
            If none of the parameters are passed, it will get all users from database.
            
            Otherwise, it will find users that will match ANY or ALL of the parameters.
            
            Notes: 
            
            - match_all can have values ( 0/no/false ) and ( 1/yes/true )
            
            - parameter matching settings:
            
            -- first_name - CONTAINS
            
            -- last_name - CONTAINS
            
            -- email - EXACT
            
            -- phone_number - CONTAINS
            
            EXACT - searching for value '123', will find only '123' and will not find
            '1234' or '0012300'. 
            
            CONTAINS - searching for value 'abc', will find 'abc', '123abc', '00abc00' etc.""")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))
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
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            )
    })
    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<?> findUserByAttributes(@RequestParam(name = "first_name", required = false) String firstName,
                                                  @RequestParam(name = "last_name", required = false) String lastName,
                                                  @RequestParam(name = "email", required = false) String email,
                                                  @RequestParam(name = "phone_number", required = false) String phoneNumber,
                                                  @RequestParam(name = "include", defaultValue = "ALL")IncludeContent includeContent,
                                                  @RequestParam(name = "match_all", defaultValue = "false") boolean MATCH_ALL){
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(userService.getUsersByAttributes(firstName, lastName, includeContent, email,
                phoneNumber, MATCH_ALL));
    }

    @Operation(summary = "Finds user by ID",
    description = "Just finds user by ID :)")
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
                    responseCode = "401",
                    description = "Unauthorized",
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
    @GetMapping(value = "/{userId}", produces = "application/json")
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @Operation(summary = "Updates user by ID")
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
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            )
    })
    @PutMapping(value = "/{userId}", produces = "application/json")
    public ResponseEntity<?> updateUserById(@Valid  @RequestBody UserDTO userDTO, @PathVariable("userId") Long userId){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateUserById(userId, userDTO));
    }

    @Operation(summary = "Deletes user by ID")
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
                    responseCode = "401",
                    description = "Unauthorized",
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
    @DeleteMapping(value = "/{userid}", produces = "application/json")
    public ResponseEntity<?> deleteUserById(@PathVariable("userId") Long userId, @Valid @RequestBody DeletionInfoDTO infoDTO){
        return ResponseEntity.ok(userService.deleteUserById(userId, infoDTO));
    }

}
