package com.example.benomad.service;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.enums.ReviewStatus;
import com.example.benomad.exception.ContentNotFoundException;

import java.security.Principal;
import java.util.List;

public interface BlogService {
    BlogDTO insertBlog(BlogDTO blogDTO) throws ContentNotFoundException;
    BlogDTO getBlogById(Long blogId, Principal principal) throws ContentNotFoundException;
    List<BlogDTO> getBlogsByAttributes(Long authorId, Principal principal, String title,
                                       ReviewStatus reviewStatus, boolean MATCH_ALL) throws ContentNotFoundException;
    BlogDTO likeDislikeBlogById(Long blogId, Principal principal, boolean isDislike) throws ContentNotFoundException;
    BlogDTO updateBlogById(Long blogId, BlogDTO blogDTO) throws ContentNotFoundException;
    BlogDTO deleteBlogById(Long blogId) throws ContentNotFoundException;
}
