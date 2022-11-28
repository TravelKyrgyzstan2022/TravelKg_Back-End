package com.example.benomad.controller;

import com.example.benomad.advice.ExceptionResponse;
import com.example.benomad.dto.BlogDTO;
import com.example.benomad.dto.DeletionInfoDTO;
import com.example.benomad.service.impl.BlogServiceImpl;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/admin/blogs")
@Tag(name = "Admin Resource", description = "The Administrator API")
public class AdminBlogController {

    private final BlogServiceImpl blogService;

    @Operation(summary = "Inserts a blog to the database",
            description = "")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = BlogDTO.class))
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
    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<?> insertBlog(@RequestBody BlogDTO dto){
        return ResponseEntity.ok(blogService.insertBlog(dto));
    }

    @Hidden
    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> forwardSlashFix(@RequestBody BlogDTO dto){
        return ResponseEntity.ok(blogService.insertBlog(dto));
    }


    @Operation(summary = "Updates a blog by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = BlogDTO.class))
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
    @PutMapping(value = "/{blogId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateBlog(@PathVariable Long blogId, @RequestBody BlogDTO blogDTO){
        blogDTO.setId(blogId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(blogService.updateBlogById(blogId, blogDTO));
    }

    @Operation(summary = "Deletes blog by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = BlogDTO.class))
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
    @DeleteMapping(value = "/{blogId}", produces = "application/json")
    public ResponseEntity<?> deleteBlogById(@PathVariable Long blogId, @RequestBody DeletionInfoDTO infoDTO){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(blogService.deleteBlogById(blogId, infoDTO));
    }


}
