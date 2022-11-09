package com.example.benomad.controller;


import com.example.benomad.dto.CommentDTO;
import com.example.benomad.service.impl.CommentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
@Tag(name = "Comment resource", description = "The Comment API")
public class CommentController {

    private final CommentServiceImpl commentService;

    @Operation(summary = "Gets all comments")
    @GetMapping(value = {"/", ""}, produces = "application/json")
    public ResponseEntity<?> getAllComments(Principal principal){
        return ResponseEntity.ok(commentService.getAllComments(principal));
    }

    @Operation(summary = "Finds comment by id")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getCommentById(@PathVariable Long id, Principal principal){
        return ResponseEntity.ok(commentService.getCommentById(id, principal));
    }

    @Operation(summary = "Inserts a comment to the database (admin)")
    @PostMapping(value = {"/", ""}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> insertComment(@RequestBody CommentDTO commentDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                commentService.insertComment(commentDTO.getReference(), commentDTO.getReferenceId(), commentDTO));
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

    @PutMapping(value = "/like", produces = "application/json")
    public ResponseEntity<?> likeDislikeComment(@RequestParam("comment_id") Long commentId,
//                                                @RequestParam(name = "user_id", required = false) Long userId,
                                                @RequestParam(name = "is_dislike", defaultValue = "0") boolean isDislike,
                                                Principal principal){
        return ResponseEntity.ok(commentService.likeDislikeComment(commentId, isDislike, principal));
    }
}
