package com.example.benomad.service;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.enums.Status;
import com.example.benomad.exception.ContentNotFoundException;

import java.util.List;

public interface BlogService {
    BlogDTO insertBlog(BlogDTO blogDTO) throws ContentNotFoundException;
    BlogDTO getBlogById(Long blogId, Long userId) throws ContentNotFoundException;
    List<BlogDTO> getBlogsByAttributes(Long authorId, Long currentUserId, String title,
                                       Status status, boolean MATCH_ALL) throws ContentNotFoundException;
    BlogDTO likeDislikeBlogById(Long blogId, Long userId, boolean isDislike) throws ContentNotFoundException;
    BlogDTO updateBlogById(Long blogId, BlogDTO blogDTO) throws ContentNotFoundException;
    BlogDTO deleteBlogById(Long blogId) throws ContentNotFoundException;
}
