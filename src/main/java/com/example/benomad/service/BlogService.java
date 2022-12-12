package com.example.benomad.service;

import com.example.benomad.dto.*;
import com.example.benomad.entity.Blog;
import com.example.benomad.entity.Comment;
import com.example.benomad.enums.IncludeContent;
import com.example.benomad.enums.ReviewStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BlogService {
    BlogDTO getBlogById(Long blogId);
    List<BlogDTO> getMyBlogs();
    List<BlogDTO> getBlogsByAttributes(String title, IncludeContent includeContent,
                                       ReviewStatus reviewStatus, boolean MATCH_ALL);
    List<UserDTO> getAuthors(String firstName, String lastName);
    MessageResponse likeDislikeBlogById(Long blogId, boolean isDislike);
    BlogDTO updateBlogById(Long blogId, BlogDTO blogDTO);
    BlogDTO deleteBlogById(Long blogId, DeletionInfoDTO infoDTO);
    MessageResponse approveBlog(Long blogId);
    MessageResponse rejectBlog(Long blogId);
    List<BlogDTO> getBlogsByAuthorId(Long userId);
    MessageResponse insertImagesByBlogId(Long blogId, MultipartFile[] files);
    List<String> getImagesById(Long id);
    BlogDTO insertBlogWithImages(BlogDTO blogDTO, MultipartFile[] files);
    MessageResponse insertImages64ByBlogId(Long id, ImageDTO[] files);
    Blog getBlogEntityById(Long blogId);
    void addComment(Long blogId, Comment comment);
}
