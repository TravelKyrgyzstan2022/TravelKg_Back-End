package com.example.benomad.mapper;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.entity.Blog;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.enums.ReviewStatus;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.repository.BlogRepository;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogMapper {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthServiceImpl authService;
    private final DeletionInfoMapper deletionInfoMapper;

    public BlogDTO entityToDto(Blog blog){
        Long userId = authService.getCurrentUserId();
        return BlogDTO.builder()
                .id(blog.getId())
                .reviewStatus(blog.getReviewStatus())
                .title(blog.getTitle())
                .body(blog.getBody())
                .creationDate(blog.getCreationDate())
                .updateDate(blog.getUpdateDate())
                .isDeleted(blog.isDeleted())
                .deletionInfoDTO(blog.getDeletionInfo() != null ?
                        deletionInfoMapper.entityToDto(blog.getDeletionInfo()) : null)
                .authorId((userMapper.entityToDto(blog.getAuthor()).getId()))
                .likes(blogRepository.getLikesNumberById(blog.getId()))
                .isLikedByCurrentUser(userId != null ?
                        blogRepository.isBlogLikedByUser(blog.getId(), userId) : null)
                .imageUrl(blog.getImageUrl().orElse(null))
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
                                    throw new ContentNotFoundException(ContentNotFoundEnum.USER, "id", String.valueOf(blogDTO.getAuthorId()));
                                })
                )
                .creationDate(blogDTO.getCreationDate())
                .updateDate(blogDTO.getUpdateDate())
                .isDeleted(blogDTO.isDeleted())
                .deletionInfo(blogDTO.getDeletionInfoDTO() != null ?
                        deletionInfoMapper.dtoToEntity(blogDTO.getDeletionInfoDTO()) : null)
                .title(blogDTO.getTitle())
                .body(blogDTO.getBody())
                .reviewStatus(blogDTO.getReviewStatus())
                .imageUrl(blogDTO.getImageUrl())
                .build();
    }

    public List<BlogDTO> entityListToDtoList(List<Blog> entities){
        return entities.stream().map(this::entityToDto).collect(Collectors.toList());
    }
}
