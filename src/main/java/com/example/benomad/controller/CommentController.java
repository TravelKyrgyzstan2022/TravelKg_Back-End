package com.example.benomad.controller;


import com.example.benomad.exception.NotFoundException;
import com.example.benomad.dto.CommentDTO;
import com.example.benomad.service.impl.CommentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Data
@RequestMapping("/api/v1/comments")
@Tag(name = "Comment resource", description = "The Comment API ")
public class CommentController {
    private final CommentServiceImpl commentServiceImpl;

    @GetMapping("")
    void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/v1/comments/");
    }

    @Operation(summary = "Gets all comments")
    @GetMapping( produces = "application/json")
    public ResponseEntity<?> getAllComments(){
        try{
            return ResponseEntity.ok(commentServiceImpl.getAllComments());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Finds comment by id")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getCommentById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(commentServiceImpl.getCommentById(id));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Inserts comment into database")
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> insertComment(@RequestBody CommentDTO commentDTO){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(commentServiceImpl.insertComment(commentDTO));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Deletes comment by ID")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteCommentById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(commentServiceImpl.deleteCommentById(id));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "Updates comment by ID")
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updatePlaceById(@PathVariable Long id, @RequestBody CommentDTO commentDTO){
        try{
            return ResponseEntity.ok(commentServiceImpl.updateCommentById(id,commentDTO));
        }catch (NotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
