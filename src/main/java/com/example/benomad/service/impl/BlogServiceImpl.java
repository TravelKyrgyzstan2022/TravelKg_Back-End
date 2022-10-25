package com.example.benomad.service.impl;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.entity.Blog;
import com.example.benomad.enums.Status;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.mapper.BlogMapper;
import com.example.benomad.repository.BlogRepository;
import com.example.benomad.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;

    @Override
    public BlogDTO getBlogById(Long blogId, Long userId) throws ContentNotFoundException {
        return BlogMapper.entityToDto(blogRepository.findById(blogId)
                .orElseThrow(ContentNotFoundException::new), blogRepository.isBlogLikedByUser(blogId, userId),
                blogRepository.getLikesNumberById(blogId));
    }

    @Override
    public List<BlogDTO> getBlogs(Long userId) {
        return null;
    }

    @Override
    public List<BlogDTO> getBlogsByAuthorId(Long blogId, Long userId) {
        return null;
    }

    @Override
    public List<BlogDTO> getBlogsByTitle(String title, Long userId) {
        return null;
    }

    @Override
    public List<BlogDTO> getBlogsByStatus(Status status, Long userId) {
        return null;
    }

    @Override
    public boolean checkBlogForLikeById(Long blogId, Long userId) throws ContentNotFoundException {
        return false;
    }

    @Override
    public void likeDislikeBlogById(Long blogId, Long userId, boolean isDislike) throws ContentNotFoundException {

    }

    @Override
    public BlogDTO updateBlogById(BlogDTO blogDTO) throws ContentNotFoundException {
        return null;
    }

    @Override
    public void deleteBlogById(Long id) throws ContentNotFoundException {

    }
}
