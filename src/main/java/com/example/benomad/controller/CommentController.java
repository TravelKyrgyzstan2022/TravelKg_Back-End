package com.example.benomad.controller;


import com.example.benomad.dto.CommentDTO;
import com.example.benomad.service.impl.CommentServiceImpl;
import io.swagger.v3.oas.annotations.Hidden;
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
@Tag(name = "Comment resource (in progress)", description = "The Comment API ")
public class CommentController {
    private final CommentServiceImpl commentService;

    @Hidden
    @GetMapping("")
    void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/v1/comments/");
    }

    @Operation(summary = "Gets all comments")
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> getAllComments(){
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @Operation(summary = "Finds comment by id")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getCommentById(@PathVariable Long id){
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @Operation(summary = "Inserts a comment to the database")
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> insertComment(@RequestBody CommentDTO commentDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.insertComment(commentDTO));
    }

    @Operation(summary = "Deletes comment by ID")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteCommentById(@PathVariable Long id){
        return ResponseEntity.ok(commentService.deleteCommentById(id));
    }

    @Operation(summary = "Updates comment by ID")
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updatePlaceById(@PathVariable Long id, @RequestBody CommentDTO commentDTO){
        return ResponseEntity.ok(commentService.updateCommentById(id,commentDTO));
    }
}
