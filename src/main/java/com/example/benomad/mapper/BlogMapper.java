package com.example.benomad.mapper;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.entity.Blog;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.enums.ReviewStatus;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.repository.BlogRepository;
import com.example.benomad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogMapper {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public BlogDTO entityToDto(Blog blog, Long userId){
        return BlogDTO.builder()
                .id(blog.getId())
                .reviewStatus(blog.getReviewStatus())
                .title(blog.getTitle())
                .body(blog.getBody())
                .authorId((userMapper.entityToDto(blog.getAuthor()).getId()))
                .likes(blogRepository.getLikesNumberById(blog.getId()))
                .isLikedByCurrentUser(userId != null ?
                        blogRepository.isBlogLikedByUser(blog.getId(), userId) : null)
                .build();
    }

    public Blog dtoToEntity(BlogDTO blogDTO){
        if(blogDTO.getReviewStatus() == null){
            blogDTO.setReviewStatus(ReviewStatus.PENDING);
        }
        return Blog.builder()
                .id(blogDTO.getId())
                .author(
                        userRepository.findById(blogDTO.getAuthorId()).orElseThrow(
                                () -> {
                                    throw new ContentNotFoundException(ContentNotFoundEnum.USER, blogDTO.getAuthorId());
                                })
                )
                .title(blogDTO.getTitle())
                .body(blogDTO.getBody())
                .reviewStatus(blogDTO.getReviewStatus())
                .build();
    }

    public List<BlogDTO> entityListToDtoList(List<Blog> entities, Long userId){
        return entities.stream().map(entity -> entityToDto(entity, userId)).collect(Collectors.toList());
    }
}
