package com.example.benomad.mapper;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.entity.Blog;
import com.example.benomad.enums.ReviewStatus;
import com.example.benomad.repository.BlogRepository;
import com.example.benomad.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogMapper {

    private final BlogRepository blogRepository;
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
                .isDeleted(blog.getIsDeleted())
                .deletionInfoDTO(blog.getDeletionInfo() != null ?
                        deletionInfoMapper.entityToDto(blog.getDeletionInfo()) : null)
                .author((userMapper.entityToDto(blog.getAuthor())))
                .likes(blogRepository.getLikesNumberById(blog.getId()))
                .isLikedByCurrentUser(userId != null ?
                        blogRepository.isBlogLikedByUser(blog.getId(), userId) : null)
                .imageUrls(blog.getImageUrls())
                .build();
    }

    public Blog dtoToEntity(BlogDTO blogDTO){
        if(blogDTO.getReviewStatus() == null){
            blogDTO.setReviewStatus(ReviewStatus.PENDING);
        }
        return Blog.builder()
                .id(blogDTO.getId())
                .creationDate(blogDTO.getCreationDate())
                .updateDate(blogDTO.getUpdateDate())
                .isDeleted(blogDTO.getIsDeleted())
                .deletionInfo(blogDTO.getDeletionInfoDTO() != null ?
                        deletionInfoMapper.dtoToEntity(blogDTO.getDeletionInfoDTO()) : null)
                .title(blogDTO.getTitle())
                .body(blogDTO.getBody())
                .reviewStatus(blogDTO.getReviewStatus())
                .imageUrls(blogDTO.getImageUrls())
                .build();
    }

    public List<BlogDTO> entityListToDtoList(List<Blog> entities){
        return entities.stream().map(this::entityToDto).collect(Collectors.toList());
    }
}
