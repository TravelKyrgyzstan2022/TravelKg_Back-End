package com.example.benomad.service;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.enums.Status;
import com.example.benomad.exception.ContentNotFoundException;

import java.util.List;

public interface BlogService {
    BlogDTO getBlogById(Long blogId, Long userId) throws ContentNotFoundException;
    List<BlogDTO> getBlogs(Long userId);
    List<BlogDTO> getBlogsByAuthorId(Long blogId, Long userId) throws ContentNotFoundException;
    List<BlogDTO> getBlogsByTitle(String title, Long userId);
    List<BlogDTO> getBlogsByStatus(Status status, Long userId);
    boolean checkBlogForLikeById(Long blogId, Long userId) throws ContentNotFoundException;
    void likeDislikeBlogById(Long blogId, Long userId, boolean isDislike) throws ContentNotFoundException;
    BlogDTO updateBlogById(BlogDTO blogDTO) throws ContentNotFoundException;
    void deleteBlogById(Long blogId) throws ContentNotFoundException;

}
