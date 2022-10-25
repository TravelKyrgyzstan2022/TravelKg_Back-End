package com.example.benomad.mapper;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.entity.Blog;
import com.example.benomad.enums.Status;

public class BlogMapper {

    public static BlogDTO entityToDto(Blog blog, boolean isLikedByUser, Long likeCount){
        return BlogDTO.builder()
                .id(blog.getId())
                .status(blog.getStatus())
                .title(blog.getTitle())
                .body(blog.getBody())
                .authorDTO((UserMapper.entityToDto(blog.getAuthor())))
                .likes(likeCount)
                .isLikedByCurrentUser(isLikedByUser)
                .build();
    }

    public Blog dtoToEntity(BlogDTO blogDTO){
        if(blogDTO.getStatus() == null){
            blogDTO.setStatus(Status.BEING_REVIEWED);
        }
        return Blog.builder()
                .id(blogDTO.getId())
                .author(UserMapper.dtoToEntity(blogDTO.getAuthorDTO()))
                .title(blogDTO.getTitle())
                .body(blogDTO.getBody())
                .status(blogDTO.getStatus())
                .build();
    }
}
