package com.example.benomad.service;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.dto.DeletionInfoDTO;
import com.example.benomad.enums.ReviewStatus;
import com.example.benomad.exception.ContentNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface BlogService {
    BlogDTO insertBlog(BlogDTO blogDTO) throws ContentNotFoundException;
    BlogDTO getBlogById(Long blogId) throws ContentNotFoundException;
    List<BlogDTO> getMyBlogs();
    BlogDTO updateMyBlogById(BlogDTO blogDTO, Long blogId);
    BlogDTO deleteMyBlogById(Long blogId);
    List<BlogDTO> getBlogsByAttributes(Long authorId, String title,
                                       ReviewStatus reviewStatus, boolean MATCH_ALL) throws ContentNotFoundException;
    BlogDTO likeDislikeBlogById(Long blogId, boolean isDislike) throws ContentNotFoundException;
    BlogDTO updateBlogById(Long blogId, BlogDTO blogDTO) throws ContentNotFoundException;
    BlogDTO deleteBlogById(Long blogId, DeletionInfoDTO infoDTO) throws ContentNotFoundException;;
    boolean insertImagesByBlogId(Long blogId, MultipartFile[] files) throws ContentNotFoundException;
    List<String> getImagesById(Long id) throws ContentNotFoundException;
}
