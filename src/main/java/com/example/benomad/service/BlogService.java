package com.example.benomad.service;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.dto.DeletionInfoDTO;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.dto.UserDTO;
import com.example.benomad.enums.IncludeContent;
import com.example.benomad.enums.ReviewStatus;
import com.example.benomad.exception.ContentNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface BlogService {
    BlogDTO insertBlog(BlogDTO blogDTO) throws ContentNotFoundException;
    BlogDTO getBlogById(Long blogId) throws ContentNotFoundException;
    List<BlogDTO> getMyBlogs();
    BlogDTO updateMyBlogById(BlogDTO blogDTO, Long blogId);
    BlogDTO deleteMyBlogById(Long blogId);
    List<BlogDTO> getBlogsByAttributes(String title, IncludeContent includeContent,
                                       ReviewStatus reviewStatus, boolean MATCH_ALL) throws ContentNotFoundException;
    MessageResponse likeDislikeBlogById(Long blogId, boolean isDislike) throws ContentNotFoundException;
    BlogDTO updateBlogById(Long blogId, BlogDTO blogDTO) throws ContentNotFoundException;
    BlogDTO deleteBlogById(Long blogId, DeletionInfoDTO infoDTO) throws ContentNotFoundException;
    MessageResponse approveBlog(Long blogId);
    MessageResponse rejectBlog(Long blogId);
    List<BlogDTO> getBlogsByAuthorId(Long userId);
    boolean insertImagesByBlogId(Long blogId, MultipartFile[] files) throws ContentNotFoundException;
    List<String> getImagesById(Long id) throws ContentNotFoundException;
    boolean insertMyBlogWithImages(BlogDTO blogDTO, MultipartFile[] files);
}
