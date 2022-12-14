package com.example.benomad.controller;

import com.example.benomad.advice.ExceptionResponse;
import com.example.benomad.dto.BlogDTO;
import com.example.benomad.dto.ImageDTO;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.service.impl.BlogServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/blogs")
@Tag(name = "User Resource", description = "The User API ")
public class UserBlogController {

    private final BlogServiceImpl blogService;

    @Operation(summary = "Get my blogs")
    @GetMapping(value = "")
    public ResponseEntity<List<BlogDTO>> getMyBlogs(){
        return ResponseEntity.ok(blogService.getMyBlogs());
    }

    @Operation(summary = "Update my blog by ID")
    @PutMapping(value = "/{blogId}")
    public ResponseEntity<BlogDTO> updateMyBlogById(@PathVariable("blogId") Long blogId,
                                              @Valid  @RequestBody BlogDTO blogDTO){
        return ResponseEntity.ok(blogService.updateBlogById(blogId, blogDTO));
    }

    @Operation(summary = "Insert new blog")
    @PostMapping(value = {"/",""})
    public ResponseEntity<?> insertMyBlog(@Valid @RequestBody BlogDTO blogDTO){
        return ResponseEntity.ok(blogService.insertBlog(blogDTO));
    }

    @Operation(summary = "Delete my blog by ID")
    @DeleteMapping(value = "/{blogId}")
    public ResponseEntity<BlogDTO> deleteMyBlogId(@PathVariable("blogId") Long blogId){
        return ResponseEntity.ok(blogService.deleteBlogById(blogId, null));
    }


    @Operation(summary = "Inserts images to blog")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))
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
    @PutMapping(value = {"/{id}/images64","{id}/images64"})
    public ResponseEntity<?> insertImagesToBlog(@Valid @RequestBody ImageDTO[] files,@PathVariable("id") Long id) {
        return ResponseEntity.ok(blogService.insertImages64ByBlogId(id, files));
    }

    @Operation(summary = "Inserts new blog with images")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = boolean.class))
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
    @PostMapping(value = {"multipart","/multipart"},consumes = { "multipart/form-data","application/json" })
    public ResponseEntity<?> insertBlogWithImages(@RequestPart("blogDTO") BlogDTO blogDTO, @RequestPart() MultipartFile[] files){
        return ResponseEntity.ok(blogService.insertBlogWithImages(blogDTO, files));
    }

    @Operation(summary = "Inserts images by blog id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = boolean.class))
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
    @PutMapping(value = "/{blogId}/images", produces = "application/json")
    public ResponseEntity<?> insertImagesByBlogId(@PathVariable("blogId") Long blogId, @RequestParam("files") MultipartFile[] files){
        return ResponseEntity.ok(blogService.insertImagesByBlogId(blogId, files));
    }



}
