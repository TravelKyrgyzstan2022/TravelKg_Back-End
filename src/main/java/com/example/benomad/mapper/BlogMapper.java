package com.example.benomad.mapper;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.entity.Blog;
import com.example.benomad.enums.Status;
import com.example.benomad.exception.UserNotFoundException;
import com.example.benomad.repository.BlogRepository;
import com.example.benomad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogMapper {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public BlogDTO entityToDto(Blog blog, Long currentUserId){
        return BlogDTO.builder()
                .id(blog.getId())
                .status(blog.getStatus())
                .title(blog.getTitle())
                .body(blog.getBody())
                .authorId((userMapper.entityToDto(blog.getAuthor()).getId()))
                .likes(blogRepository.getLikesNumberById(blog.getId()))
                .isLikedByCurrentUser(blogRepository.isBlogLikedByUser(blog.getId(), currentUserId))
                .build();
    }

    public Blog dtoToEntity(BlogDTO blogDTO){
        if(blogDTO.getStatus() == null){
            blogDTO.setStatus(Status.BEING_REVIEWED);
        }
        return Blog.builder()
                .id(blogDTO.getId())
                .author(userRepository.findById(blogDTO.getAuthorId()).orElseThrow(UserNotFoundException::new))
                .title(blogDTO.getTitle())
                .body(blogDTO.getBody())
                .status(blogDTO.getStatus())
                .build();
    }

    public List<BlogDTO> entityListToDtoList(List<Blog> entities, Long currentUserId){
        List<BlogDTO> dtos = new ArrayList<>();
        for(Blog e : entities){
            dtos.add(entityToDto(e, currentUserId));
        }
        return dtos;
    }

    public List<Blog> dtoListToEntityList(List<BlogDTO> dtos, UserRepository userRepository){
        List<Blog> entities = new ArrayList<>();
        for(BlogDTO d: dtos){
            entities.add(dtoToEntity(d));
        }
        return entities;
    }
}
