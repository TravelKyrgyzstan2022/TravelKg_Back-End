package com.example.benomad.controller;

import com.example.benomad.advice.ExceptionResponse;
import com.example.benomad.dto.DeletionInfoDTO;
import com.example.benomad.dto.UserDTO;
import com.example.benomad.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
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


@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User Resource", description = "The User API ")
public class UserController {

    private final UserServiceImpl userService;

    @Operation(summary = "Gets all users / Finds users by attributes",
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
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content
            )
    })
    @GetMapping(value = {"/", ""}, produces = "application/json")
    public ResponseEntity<?> findUserByAttributes(@RequestParam(name = "first_name", required = false) String firstName,
                                          @RequestParam(name = "last_name", required = false) String lastName,
                                          @RequestParam(name = "email", required = false) String email,
                                          @RequestParam(name = "phone_number", required = false) String phoneNumber,
                                          @RequestParam(name = "match_all", defaultValue = "false") boolean MATCH_ALL){
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok(userService.getUsersByAttributes(firstName, lastName, email, phoneNumber,
                MATCH_ALL));
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
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long userId){
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    //fixme
    @Hidden
    @GetMapping(value = "/exact", produces = "application/json")
    public ResponseEntity<?> getUserByExactAttributes(){
        return ResponseEntity.ok(userService.getUserById(1L));
    }

    //fixme
//    @Operation(summary = "Inserts a user to the database")
//    @PostMapping(value = {"/", ""}, consumes = "application/json", produces = "application/json")
//    public ResponseEntity<?> insertUser(@RequestBody UserDTO userDTO){
//        return ResponseEntity.status(HttpStatus.CREATED).body(userService.insertUser(userDTO));
//    }

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
    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> updateUserById(@RequestBody UserDTO userDTO, @PathVariable("id") Long userId){
        userDTO.setId(userId);
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
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Long userId, @RequestBody DeletionInfoDTO infoDTO){
        return ResponseEntity.ok(userService.deleteUserById(userId, infoDTO));
    }



}
