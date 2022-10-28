package com.example.benomad.controller;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.enums.Status;
import com.example.benomad.service.impl.BlogServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/blogs")
public class BlogController {

    private final BlogServiceImpl blogService;

    @GetMapping("")
    void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/v1/blogs/");
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> findBlogs(@RequestParam(name = "author_id", required = false) Long authorId,
                                       @RequestParam(name = "title", required = false) String title,
                                       @RequestParam(name = "status", required = false) Status status,
                                       @RequestParam(name = "current_user_id", defaultValue = "1") Long userId,
                                       @RequestParam(name = "match_all", defaultValue = "false") boolean MATCH_ALL){
        return ResponseEntity.status(HttpStatus.OK).body(blogService.getBlogsByAttributes(
                authorId, userId, title, status, MATCH_ALL));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> findBlogById(@PathVariable Long id,
                                          @RequestParam(value = "current_user_id", defaultValue = "1L") Long userId){
        return ResponseEntity.status(HttpStatus.OK).body(blogService.getBlogById(id, userId));
    }

    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> insertBlog(@RequestBody BlogDTO dto){
        return ResponseEntity.ok(blogService.insertBlog(dto));
    }

    @PutMapping("/like")
    public ResponseEntity<?> likeDislikeBlog(@RequestParam(name = "blog_id") Long blogId,
                                      @RequestParam(name = "user_id") Long userId,
                                      @RequestParam(name = "is_dislike") boolean isDislike){
        blogService.likeDislikeBlogById(blogId, userId, isDislike);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("REQUEST ACCEPTED!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBlog(@RequestBody BlogDTO blogDTO){
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlogById(@PathVariable Long id){
        return null;
    }
}
