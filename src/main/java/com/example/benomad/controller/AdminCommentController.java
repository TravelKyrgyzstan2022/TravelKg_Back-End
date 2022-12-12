package com.example.benomad.controller;


import com.example.benomad.advice.ExceptionResponse;
import com.example.benomad.dto.CommentDTO;
import com.example.benomad.dto.DeletionInfoDTO;
import com.example.benomad.service.impl.CommentServiceImpl;
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
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/comments")
@Tag(name = "Admin Resource", description = "The Administrator API")
public class AdminCommentController {

    private final CommentServiceImpl commentService;

    @Operation(summary = "Gets all comments")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CommentDTO.class)))
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
    @GetMapping(value = {""}, produces = "application/json")
    public ResponseEntity<?> getAllComments(){
        return ResponseEntity.ok(commentService.getAllComments());
    }

    @Operation(summary = "Finds comment by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = CommentDTO.class))
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
    @GetMapping(value = "/{commentId}", produces = "application/json")
    public ResponseEntity<?> getCommentById(@PathVariable("commentId") Long commentId){
        return ResponseEntity.ok(commentService.getCommentById(commentId));
    }

    @Operation(summary = "Inserts a comment to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = CommentDTO.class))
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
    @PostMapping(value = {""}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> insertComment(@RequestBody CommentDTO commentDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                commentService.insertComment(commentDTO.getReference(), commentDTO.getReferenceId(), commentDTO));
    }

    @Operation(summary = "Deletes comment by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = CommentDTO.class))
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
    @DeleteMapping(value = "/{commentId}", produces = "application/json")
    public ResponseEntity<?> deleteCommentById(@PathVariable Long commentId,
                                               @RequestBody DeletionInfoDTO infoDTO){
        return ResponseEntity.ok(commentService.deleteCommentById(commentId, infoDTO));
    }

    @Operation(summary = "Updates comment by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = CommentDTO.class))
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
    @PutMapping(value = "/{commentId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateCommentById(@PathVariable("commentId") Long commentId,
                                               @RequestBody CommentDTO commentDTO){
        return ResponseEntity.ok(commentService.updateCommentById(commentId, commentDTO));
    }
}
