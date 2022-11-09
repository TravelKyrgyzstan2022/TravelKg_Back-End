package com.example.benomad.controller;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User Resource", description = "The User API ")
public class UserController {

    private final UserServiceImpl userService;

    @Operation(summary = "Gets all users / Finds users by attributes  (will be finished in the next update)")
    @GetMapping(value = {"/", ""}, produces = "application/json")
    public ResponseEntity<?> findUserByAttributes(@RequestParam(name = "first_name", required = false) String firstName,
                                          @RequestParam(name = "last_name", required = false) String lastName,
                                          @RequestParam(name = "email", required = false) String email,
                                          @RequestParam(name = "phone_number", required = false) String phoneNumber,
                                          @RequestParam(name = "match_all", defaultValue = "false") boolean MATCH_ALL){
        return ResponseEntity.ok(userService.getUsersByAttributes(firstName, lastName, email, phoneNumber,
                MATCH_ALL));
    }

    @Operation(summary = "Finds user by ID")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Inserts a user to the database")
    @PostMapping(value = {"/", ""}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> insertUser(@RequestBody UserDTO userDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.insertUser(userDTO));
    }

    @Operation(summary = "Updates user by ID")
    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> updateUserById(@RequestBody UserDTO userDTO, @PathVariable Long id){
        userDTO.setId(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateUserById(id, userDTO));
    }

    @Operation(summary = "Deletes user by ID")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.deleteUserById(id));
    }

}
