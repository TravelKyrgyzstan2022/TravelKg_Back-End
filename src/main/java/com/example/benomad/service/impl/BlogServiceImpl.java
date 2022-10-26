package com.example.benomad.service.impl;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.entity.Blog;
import com.example.benomad.enums.Status;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.mapper.BlogMapper;
import com.example.benomad.repository.BlogRepository;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @Override
    public BlogDTO getBlogById(Long blogId, Long userId) throws ContentNotFoundException {
        return BlogMapper.entityToDto(blogRepository.findById(blogId)
                .orElseThrow(ContentNotFoundException::new), blogRepository.isBlogLikedByUser(blogId, userId),
                blogRepository.getLikesNumberById(blogId));
    }

    @Override
    public List<BlogDTO> getBlogs(Long userId){
        List<BlogDTO> dtos = BlogMapper.entityListToDtoList(blogRepository.findAll());
        addIsLikedAndLikesCountToList(dtos, userId);
        return dtos;
    }

    @Override
    public List<BlogDTO> getBlogsByAuthorId(Long authorId, Long userId) throws ContentNotFoundException {
        List<Blog> entities = blogRepository.findAllByAuthor(
                userRepository.findById(authorId).orElseThrow(ContentNotFoundException::new));
        List<BlogDTO> dtos = BlogMapper.entityListToDtoList(entities);
        addIsLikedAndLikesCountToList(dtos, userId);
        return dtos;
    }

    @Override
    public List<BlogDTO> getBlogsByTitle(String title, Long userId) {
        List<Blog> entities = blogRepository.findAllByTitle(title);
        List<BlogDTO> dtos = BlogMapper.entityListToDtoList(entities);
        addIsLikedAndLikesCountToList(dtos, userId);
        return dtos;
    }

    @Override
    public List<BlogDTO> getBlogsByStatus(Status status, Long userId) {
        List<Blog> entities = blogRepository.findAllByStatus(status);
        List<BlogDTO> dtos = BlogMapper.entityListToDtoList(entities);
        addIsLikedAndLikesCountToList(dtos, userId);
        return dtos;
    }

    @Override
    public boolean checkBlogForLikeById(Long blogId, Long userId) throws ContentNotFoundException {
        Blog blog = blogRepository.findById(blogId).orElseThrow(ContentNotFoundException::new);
        return blogRepository.isBlogLikedByUser(blogId, userId);
    }

    @Override
    public void likeDislikeBlogById(Long blogId, Long userId, boolean isDislike) throws ContentNotFoundException {
        Blog blog = blogRepository.findById(blogId).orElseThrow(ContentNotFoundException::new);
        if(isDislike){
            blogRepository.dislikeBlogById(blogId, userId);
        }else{
            blogRepository.likeBlogById(blogId, userId);
        }
    }

    @Override
    public BlogDTO updateBlogById(BlogDTO blogDTO) throws ContentNotFoundException {
        Blog check = blogRepository.findById(blogDTO.getId()).orElseThrow(ContentNotFoundException::new);
        blogRepository.save(BlogMapper.dtoToEntity(blogDTO));
        addIsLikedAndLikesCount(blogDTO, null);
        return blogDTO;
    }

    @Override
    public void deleteBlogById(Long id) throws ContentNotFoundException {
        Blog blog = blogRepository.findById(id).orElseThrow(ContentNotFoundException::new);
        blogRepository.delete(blog);
    }

    public boolean checkBlogForLikeByIdWithoutException(Long blogId, Long userId){
        return blogRepository.isBlogLikedByUser(blogId, userId);
    }

    private void addIsLikedAndLikesCountToList(List<BlogDTO> dtos, Long userId){
        for(BlogDTO d : dtos){
            addIsLikedAndLikesCount(d, userId);
        }
    }

    private void addIsLikedAndLikesCount(BlogDTO dto, Long userId){
        dto.setIsLikedByCurrentUser(checkBlogForLikeByIdWithoutException(dto.getId(), userId));
        dto.setLikes(blogRepository.getLikesNumberById(dto.getId()));
    }
}
