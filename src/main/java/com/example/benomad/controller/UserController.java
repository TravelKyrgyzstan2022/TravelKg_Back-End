package com.example.benomad.controller;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Resource", description = "The User API ")
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("")
    void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/v1/users/");
    }

    @Operation(summary = "Finds all users or users by given attributes")
    @GetMapping("/")
    public ResponseEntity<?> findUserByAttributes(@RequestParam(name = "id", required = false) Long id,
                                          @RequestParam(name = "login", required = false) String login,
                                          @RequestParam(name = "first_name", required = false) String firstName,
                                          @RequestParam(name = "last_name", required = false) String lastName,
                                          @RequestParam(name = "email", required = false) String email,
                                          @RequestParam(name = "phone_number", required = false) String phoneNumber){
        if(id != null){
            return ResponseEntity.ok(userService.getUserById(id));
        }
        if(login != null){
            return ResponseEntity.ok(userService.getUserByLogin(login));
        }
        if(email != null){
            return ResponseEntity.ok(userService.getUserByEmail(email));
        }
        if(phoneNumber != null){
            return ResponseEntity.ok(userService.getUserByPhoneNumber(phoneNumber));
        }
        if(firstName != null && lastName != null){
            return ResponseEntity.ok(userService.getUsersByFirstNameAndLastName(firstName, lastName));
        }
        if(firstName != null){
            return ResponseEntity.ok(userService.getUsersByFirstName(firstName));
        }
        if(lastName != null){
            return ResponseEntity.ok(userService.getUsersByLastName(lastName));
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/")
    public ResponseEntity<?> insertUser(@RequestBody UserDTO userDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(userDTO));
    }

    @Operation(summary = "Updates user by ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserById(@RequestBody UserDTO userDTO, @PathVariable Long id){
        userDTO.setId(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateUserById(id, userDTO));
    }

    @Operation(summary = "Deletes user by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.deleteUserById(id));
    }

}
