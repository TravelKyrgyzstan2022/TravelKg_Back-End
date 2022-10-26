package com.example.benomad.controller;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.entity.Blog;
import com.example.benomad.enums.Status;
import com.example.benomad.exception.ContentNotFoundException;
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
                                       @RequestParam(name = "status", defaultValue = "APPROVED") Status status,
                                       @RequestParam(name = "current_user_id") Long userId,
                                       @RequestParam(name = "only_liked", defaultValue = "false") boolean onlyLiked)
            throws ContentNotFoundException {
        try{
            if(authorId != null){
                return ResponseEntity.status(HttpStatus.OK).body(blogService.getBlogsByAuthorId(authorId, userId));
            }

        }catch (Exception e){
            return null;
        }
        return null;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> findBlogById(@PathVariable Long id,
                                          @RequestParam("current_user_id") Long userId){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(blogService.getBlogById(id, userId));
        }catch (Exception e){
            return null;
        }
    }

    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> insertBlog(@RequestBody Blog blog){
        return null;
    }

    @PutMapping("/like")
    public ResponseEntity<?> likeDislikeBlog(@RequestParam(name = "blog_id") Long blogId,
                                      @RequestParam(name = "user_id") Long userId,
                                      @RequestParam(name = "is_dislike") boolean isDislike){
        return null;
    }

    @PutMapping("/")
    public ResponseEntity<?> updateBlog(@RequestBody BlogDTO blogDTO){
        return null;
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteBlog(@RequestParam("id") Long id){
        return null;
    }
}
