package com.example.benomad.service;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.enums.Status;
import com.example.benomad.exception.BlogNotFoundException;

import java.util.List;

public interface BlogService {
    BlogDTO insertBlog(BlogDTO blogDTO);
    BlogDTO getBlogById(Long blogId, Long userId) throws BlogNotFoundException;
    List<BlogDTO> getBlogsByAttributes(Long authorId, Long currentUserId, String title,
                                       Status status, boolean MATCH_ALL) throws BlogNotFoundException;
    List<BlogDTO> getBlogs(Long currentUserId);
    void likeDislikeBlogById(Long blogId, Long userId, boolean isDislike) throws BlogNotFoundException;
    BlogDTO updateBlogById(BlogDTO blogDTO) throws BlogNotFoundException;
    void deleteBlogById(Long blogId) throws BlogNotFoundException;

}
