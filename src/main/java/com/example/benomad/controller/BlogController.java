package com.example.benomad.controller;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.enums.Status;
import com.example.benomad.service.impl.BlogServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("api/v1/blogs")
@Tag(name = "Blog Resource", description = "The Blog API ")
public class BlogController {

    private final BlogServiceImpl blogService;

    @Operation(summary = "Gets all the blogs / Finds blogs by attributes",
    description = "Returns all blogs if none of the parameters are specified." +
            "\n*Note = current_user_id is needed to check whether the blogs are liked by the user or not.")
    @GetMapping(value = {"/", ""}, produces = "application/json")
    public ResponseEntity<?> findBlogs(@RequestParam(name = "author_id", required = false) Long authorId,
                                       @RequestParam(name = "title", required = false) String title,
                                       @RequestParam(name = "status", required = false) Status status,
                                       @RequestParam(name = "current_user_id", defaultValue = "1") Long userId,
                                       @RequestParam(name = "match_all", defaultValue = "false") boolean MATCH_ALL){
        return ResponseEntity.status(HttpStatus.OK).body(blogService.getBlogsByAttributes(
                authorId, userId, title, status, MATCH_ALL));
    }

    @Operation(summary = "Finds blog by ID",
    description = "*Note = current_user_id is needed to check whether the blogs are liked by the user or not.")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> findBlogById(@PathVariable Long id,
                                          @RequestParam(value = "current_user_id", defaultValue = "1L") Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(blogService.getBlogById(id, userId));
    }

    @Operation(summary = "Inserts a blog to the database",
    description = "")
    @PostMapping(value = {"/", ""}, produces = "application/json")
    public ResponseEntity<?> insertBlog(@RequestBody BlogDTO dto){
        return ResponseEntity.ok(blogService.insertBlog(dto));
    }

    @Operation(summary = "Likes or dislikes the blog",
            description = "Dislike is just a removal of a like, not an actual dislike :)")
    @PutMapping("/like")
    public ResponseEntity<?> likeDislikeBlog(@RequestParam(name = "blog_id") Long blogId,
                                      @RequestParam(name = "user_id") Long userId,
                                      @RequestParam(name = "is_dislike") boolean isDislike){
        blogService.likeDislikeBlogById(blogId, userId, isDislike);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("REQUEST ACCEPTED!");
    }

    @Operation(summary = "Updates a blog by ID")
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateBlog(@RequestBody BlogDTO blogDTO, @PathVariable Long id){
        blogDTO.setId(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(blogService.updateBlogById(blogDTO));
    }

    @Operation(summary = "Deletes the blog by ID")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteBlogById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(blogService.deleteBlogById(id));
    }
}
