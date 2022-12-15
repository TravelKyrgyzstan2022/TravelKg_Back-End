package com.example.benomad.controller;

import com.example.benomad.dto.CommentDTO;
import com.example.benomad.service.impl.CommentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/comments")
@Tag(name = "User Resource", description = "The User API ")
public class UserCommentController {

    private final CommentServiceImpl commentService;

    @Operation(summary = "Deletes current user's comment by ID")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentDTO> deleteMyComment(@PathVariable("commentId") Long commentId) {
        return ResponseEntity.ok(commentService.deleteCommentById(commentId, null));
    }

    @Operation(summary = "Updates current user's comment by ID")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateMyComment(@PathVariable("commentId") Long commentId,
                                                      @Valid @RequestBody CommentDTO commentDTO){
        return ResponseEntity.ok(commentService.updateCommentById(commentId, commentDTO));
    }
}
