package com.example.benomad.mapper;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.entity.Blog;
import com.example.benomad.enums.ReviewStatus;
import com.example.benomad.repository.BlogRepository;
import com.example.benomad.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogMapper {

    private final BlogRepository blogRepository;
    private final UserMapper userMapper;
    private final AuthServiceImpl authService;
    private final DeletionInfoMapper deletionInfoMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public BlogDTO entityToDto(Blog blog){
        Long userId = authService.getCurrentUserId();
        return BlogDTO.builder()
                .id(blog.getId())
                .reviewStatus(blog.getReviewStatus())
                .title(blog.getTitle())
                .body(blog.getBody())
                .creationDate(formatter.format(blog.getCreationDate()))
                .updateDate(blog.getUpdateDate() != null ? formatter.format(blog.getUpdateDate()) : null)
                .isDeleted(blog.getIsDeleted())
                .deletionInfoDTO(blog.getDeletionInfo() != null ?
                        deletionInfoMapper.entityToDto(blog.getDeletionInfo()) : null)
                .author((userMapper.entityToDto(blog.getAuthor())))
                .likeCount(blogRepository.getLikesNumberById(blog.getId()))
                .commentCount(blog.getComments().size())
                .isLikedByCurrentUser(userId != null ?
                        blogRepository.isBlogLikedByUser(blog.getId(), userId) : null)
                .imageUrls(blog.getImageUrls())
                .build();
    }

    public Blog dtoToEntity(BlogDTO blogDTO){
        return Blog.builder()
                .title(blogDTO.getTitle())
                .body(blogDTO.getBody())
                .imageUrls(blogDTO.getImageUrls())
                .build();
    }

    public List<BlogDTO> entityListToDtoList(List<Blog> entities){
        return entities.stream().map(this::entityToDto).collect(Collectors.toList());
    }
}


