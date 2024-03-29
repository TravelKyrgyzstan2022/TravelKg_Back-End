package com.example.benomad.controller;

import com.example.benomad.advice.ExceptionResponse;
import com.example.benomad.dto.ArticleDTO;
import com.example.benomad.dto.ImageDTO;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.service.impl.ArticleServiceImpl;
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

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/v1/admin/articles")
@Tag(name = "Admin Resource", description = "The Administrator API")
public class AdminArticleController {

    private final ArticleServiceImpl articleService;

    @Operation(summary = "Inserts an article to the database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = ArticleDTO.class))
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
    @PostMapping(value = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> insertArticle(@Valid @RequestBody ArticleDTO articleDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(articleService.insertArticle(articleDTO));
    }

    @Operation(summary = "Deletes article by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = ArticleDTO.class))
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
    @DeleteMapping(value = "/{articleId}", produces = "application/json")
    public ResponseEntity<?> deleteArticleById(@PathVariable("articleId") Long articleId){
        return ResponseEntity.ok(articleService.deleteArticleById(articleId));
    }

    @Operation(summary = "Updates article by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = ArticleDTO.class))
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
    @PutMapping(value = "/{articleId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateArticleById(@PathVariable("articleId") Long articleId, @Valid @RequestBody ArticleDTO articleDTO){
        return ResponseEntity.ok(articleService.updateArticleById(articleId, articleDTO));
    }

    @Operation(summary = "Inserts images by article id")
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
    @PutMapping(value = "/{articleId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/json")
    public ResponseEntity<?> insertImagesByBlogId(@PathVariable("articleId") Long articleId, @RequestParam("files") MultipartFile[] files){
        return ResponseEntity.ok(articleService.insertImagesByArticleId(articleId,files));
    }

    @Operation(summary = "Inserts new article with the images")
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
    public ResponseEntity<?> insertArticleWithImages(@RequestPart("articleDTO") ArticleDTO articleDTO, @RequestPart("files") MultipartFile[] files){
        return ResponseEntity.ok(articleService.insertArticleWithImages(articleDTO,files));
    }


    @Operation(summary = "Inserts images to article")
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
    @PutMapping(value ="/{articleId}/images64")
    public ResponseEntity<?> insertImagesToBlog(@Valid @RequestBody ImageDTO[] files, @PathVariable("articleId") Long id) {
        return ResponseEntity.ok(articleService.insertImages64ByArticleId(id,files));
    }
}
