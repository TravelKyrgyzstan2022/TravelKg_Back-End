package com.example.benomad.controller;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.dto.CommentDTO;
import com.example.benomad.enums.CommentReference;
import com.example.benomad.enums.Status;
import com.example.benomad.service.impl.BlogServiceImpl;
import com.example.benomad.service.impl.CommentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("api/v1/blogs")
@Tag(name = "Blog Resource", description = "The Blog API ")
public class BlogController {

    private final BlogServiceImpl blogService;
    private final CommentServiceImpl commentService;

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
                                          @RequestParam(value = "current_user_id", defaultValue = "1") Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(blogService.getBlogById(id, userId));
    }

    @Operation(summary = "Gets comments of blog by ID")
    @GetMapping(value = "/{id}/comments", produces = "application/json")
    public ResponseEntity<?> getBlogCommentsById(
            @PathVariable Long id,
            @RequestParam(name = "current_user_id", defaultValue = "1") Long cuserId,
            @RequestParam(name = "sort_by", required = false) Optional<String> sortBy,
            @RequestParam(name = "page", required = false) Optional<Integer> page,
            @RequestParam(name = "size", required = false) Optional<Integer> size){
        PageRequest pageRequest = PageRequest.of(page.orElse(0), size.orElse(1),
                Sort.by(sortBy.orElse("id")));
        return ResponseEntity.ok(commentService.getReferenceCommentsById(cuserId, id, CommentReference.BLOG, pageRequest));
    }

    @Operation(summary = "Inserts a blog to the database",
    description = "")
    @PostMapping(value = {"/", ""}, produces = "application/json")
    public ResponseEntity<?> insertBlog(@RequestBody BlogDTO dto){
        return ResponseEntity.ok(blogService.insertBlog(dto));
    }

    @PostMapping(value = "/{id}/comment", produces = "application/json")
    public ResponseEntity<?> commentBlog(@PathVariable Long blogId, @RequestBody CommentDTO commentDTO){
        return ResponseEntity.ok(commentService.insertComment(CommentReference.BLOG, blogId, commentDTO));
    }

    @Operation(summary = "Likes or dislikes the blog",
            description = "Dislike is just a removal of a like, not an actual dislike :)")
    @PutMapping("/like")
    public ResponseEntity<?> likeDislikeBlog(@RequestParam(name = "blog_id") Long blogId,
                                      @RequestParam(name = "user_id") Long userId,
                                      @RequestParam(name = "is_dislike") boolean isDislike){
        return ResponseEntity.ok(blogService.likeDislikeBlogById(blogId, userId, isDislike));
    }

    @Operation(summary = "Updates a blog by ID")
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateBlog(@PathVariable Long id, @RequestBody BlogDTO blogDTO){
        blogDTO.setId(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(blogService.updateBlogById(id, blogDTO));
    }

    @Operation(summary = "Deletes the blog by ID")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteBlogById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(blogService.deleteBlogById(id));
    }

    @Operation(summary = "Uploads image by blog ID",
            description = "Adds new image record to a blog by its ID and image itself.")
    @PutMapping(path = "/uploadImage/{userId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadUserProfileImage(@PathVariable("userId") Long id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(blogService.insertImageByBlogId(id,file));
    }

    @Operation(summary = "Gets image by by blog ID",
            description = "Adds new rating record blog a  by its ID and users ID.")
    @GetMapping(path = "/getImage/{userId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserProfileImage(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(blogService.getImageByBlogId(id));
    }
}