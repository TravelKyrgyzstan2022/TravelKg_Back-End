package com.example.benomad.controller;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.service.impl.BlogServiceImpl;
import com.sun.mail.iap.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/blogs")
@Tag(name = "User Resource", description = "The User API ")
public class UserBlogController {

    private final BlogServiceImpl blogService;

    @Operation(summary = "Get my blogs")
    @GetMapping(value = "")
    public ResponseEntity<?> getMyBlogs(){
        return ResponseEntity.ok(blogService.getMyBlogs());
    }

    @Operation(summary = "Update my blog by ID")
    @PutMapping(value = "/{blogId}")
    public ResponseEntity<?> updateMyBlogById(@PathVariable("blogId") Long blogId,
                                              @RequestBody BlogDTO blogDTO){
        return ResponseEntity.ok(blogService.updateMyBlogById(blogDTO, blogId));
    }

    @Operation(summary = "Insert new blog")
    @PostMapping(value = "/")
    public ResponseEntity<?> insertMyBlog(@RequestBody BlogDTO blogDTO){
        return ResponseEntity.ok(blogService.insertMyBlog(blogDTO));
    }

    @Operation(summary = "Delete my blog by ID")
    @DeleteMapping(value = "/{blogId}")
    public ResponseEntity<?> deleteMyBlogId(@PathVariable("blogId") Long blogId){
        return ResponseEntity.ok(blogService.deleteMyBlogById(blogId));
    }
}
