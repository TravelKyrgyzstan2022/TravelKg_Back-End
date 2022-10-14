package com.example.benomad.controller;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/")
@RequiredArgsConstructor
@Tag(name = "User Resource", description = "The User API ")
public class UserController {

    private final UserServiceImpl userService;

    @Operation(summary = "Gets all the users")
    @GetMapping("/")
    public ResponseEntity<?> getAllUsers(){
        try{
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Finds user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> findUserById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(userService.getUserById(id));
        }catch (ContentNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Updates user by ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUserById(@RequestBody UserDTO userDTO, @PathVariable Long id){
        try{
            userDTO.setId(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateUserById(id, userDTO));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Deletes user by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(userService.deleteUserById(id));
        }catch (ContentNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
