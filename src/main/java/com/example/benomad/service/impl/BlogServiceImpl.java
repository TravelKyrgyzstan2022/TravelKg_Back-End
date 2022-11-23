package com.example.benomad.service.impl;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.dto.DeletionInfoDTO;
import com.example.benomad.entity.Blog;
import com.example.benomad.enums.AwsBucket;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.enums.ImagePath;
import com.example.benomad.enums.ReviewStatus;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.exception.ContentIsAlreadyLikedException;
import com.example.benomad.exception.ContentIsNotLikedException;
import com.example.benomad.exception.FailedWhileUploadingException;
import com.example.benomad.logger.LogWriterServiceImpl;
import com.example.benomad.mapper.BlogMapper;
import com.example.benomad.mapper.DeletionInfoMapper;
import com.example.benomad.repository.BlogRepository;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final AuthServiceImpl authService;
    private final BlogMapper blogMapper;
    private final DeletionInfoMapper deletionInfoMapper;
    private final ImageServiceImpl imageService;
    private final LogWriterServiceImpl logWriter;

    @Override
    public BlogDTO insertBlog(BlogDTO blogDTO) throws ContentNotFoundException {
        blogDTO.setId(null);
        blogDTO.setAuthorId(authService.getCurrentUserId());
        blogDTO.setId(blogRepository.save(blogMapper.dtoToEntity(blogDTO)).getId());
        logWriter.insert(String.format("%s - Inserted blog with id = %d", authService.getName(), blogDTO.getId()));
        return blogDTO;
    }

    @Override
    public BlogDTO getBlogById(Long blogId) throws ContentNotFoundException {
        BlogDTO blogDTO = blogMapper.entityToDto(
                blogRepository.findById(blogId).orElseThrow(
                        () -> {
                            throw new ContentNotFoundException(ContentNotFoundEnum.BLOG, "id", String.valueOf(blogId));
                        })
        );
        logWriter.get(String.format("%s - Returned blog with id = %d", authService.getName(), blogId));
        return blogDTO;
    }

    @Override
    public List<BlogDTO> getBlogsByAttributes(Long authorId, String title,
                                              ReviewStatus reviewStatus, boolean MATCH_ALL)
            throws ContentNotFoundException {
        Long userId = authService.getCurrentUserId();
        Blog blog = Blog.builder()
                .title(title)
                .reviewStatus(reviewStatus)
                .build();

        if(authorId != null){
            blog.setAuthor(
                    userRepository.findById(authorId).orElseThrow(
                            () -> {
                                throw new ContentNotFoundException(ContentNotFoundEnum.USER, "id", String.valueOf(authorId));
                            })
            );
        }

        Example<Blog> example = Example.of(blog, getExample(MATCH_ALL));

        List<Blog> blogs = blogRepository.findAll(example);

        logWriter.get(String.format("%s - Returned %d blogs", authService.getName(), blogs.size()));

        //fixme
        System.out.println(LocalDate.now(ZoneId.of("Asia/Bishkek")));

        return blogMapper.entityListToDtoList(blogs);
    }


    @Override
    public BlogDTO likeDislikeBlogById(Long blogId, boolean isDislike) throws ContentNotFoundException {
        Long userId = authService.getCurrentUserId();

        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> {
            throw new ContentNotFoundException(ContentNotFoundEnum.BLOG, "id", String.valueOf(blogId));
        });
        boolean isAlreadyLiked = blogRepository.isBlogLikedByUser(blogId, userId);
        if(isDislike){
            if(!isAlreadyLiked){
                throw new ContentIsNotLikedException(ContentNotFoundEnum.BLOG);
            }
            blogRepository.dislikeBlogById(blogId, userId);
        }else{
            if(isAlreadyLiked){
                throw new ContentIsAlreadyLikedException(ContentNotFoundEnum.BLOG);
            }
            blogRepository.likeBlogById(blogId, userId);
        }
        logWriter.update(String.format("%s - %s blog with id = %d", authService.getName(),
                isDislike ? "Disliked" : "Liked", blogId));
        return blogMapper.entityToDto(blog);
    }

    @Override
    public BlogDTO updateBlogById(Long blogId, BlogDTO blogDTO) throws ContentNotFoundException {
        if(!blogRepository.existsById(blogId)){
            throw new ContentNotFoundException(ContentNotFoundEnum.BLOG, "id" , String.valueOf(blogId));
        }
        blogDTO.setId(blogId);
        Blog blog = blogMapper.dtoToEntity(blogDTO);
        blog.setCreationDate(null);
        blog.setUpdateDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        blogRepository.save(blog);
        addIsLikedAndLikesCount(blogDTO, authService.getCurrentUserId());
        logWriter.update(String.format("%s - Updated blog with id = %d", authService.getName(), blogId));
        return blogDTO;
    }

    @Override
    public BlogDTO deleteBlogById(Long blogId, DeletionInfoDTO infoDTO) throws ContentNotFoundException {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> {
            throw new ContentNotFoundException(ContentNotFoundEnum.BLOG, "id" , String.valueOf(blogId));
        });
        blog.setDeleted(true);
        infoDTO.setDeletionDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        blog.setDeletionInfo(deletionInfoMapper.dtoToEntity(infoDTO));
        blogRepository.save(blog);
        logWriter.delete(String.format("%s - Deleted blog with id = %d", authService.getName(), blogId));
        return blogMapper.entityToDto(blog);
    }

    @Override
    public Long insertImageByBlogId(Long id, MultipartFile file) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException(ContentNotFoundEnum.BLOG,"id",String.valueOf(id)));
        imageService.checkIsNotEmpty(file);
        imageService.checkIsImage(file);
        Map<String, String> metadata = imageService.getMetaData(file);
        String pathToFile = String.format("%s/%s", AwsBucket.MAIN_BUCKET.getBucketName(), ImagePath.BLOG.getPathToImage());
        String uniqueFileName = String.format("%s-%s",file.getOriginalFilename(), UUID.randomUUID());
        try {
            imageService.saveImageAws(pathToFile,uniqueFileName, Optional.of(metadata),file.getInputStream());
            blog.setImageUrl(uniqueFileName);
            blogRepository.save(blog);
            return blog.getId();
        } catch (IOException e) {
            throw new FailedWhileUploadingException();
        }
    }

    @Override
    public byte[] getImageByBlogId(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException(ContentNotFoundEnum.PLACE,"id",String.valueOf(id)));
        String pathToFile = String.format("%s/%s", AwsBucket.MAIN_BUCKET.getBucketName(), ImagePath.BLOG.getPathToImage());
        return blog.getImageUrl().map(x -> imageService.getAwsImageByPathAndKey(pathToFile,x)).orElse(new byte[0]);
    }

    private boolean checkBlogForLikeByIdWithoutException(Long blogId, Long userId){
        return blogRepository.isBlogLikedByUser(blogId, userId);
    }

    private void addIsLikedAndLikesCount(BlogDTO dto, Long userId){
        dto.setIsLikedByCurrentUser(checkBlogForLikeByIdWithoutException(dto.getId(), userId));
        dto.setLikes(blogRepository.getLikesNumberById(dto.getId()));
    }

    private ExampleMatcher getExample(boolean MATCH_ALL){
        ExampleMatcher MATCHER_ANY = ExampleMatcher.matchingAny()
                .withMatcher("author", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("status", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnorePaths("id", "likedUsers");
        ExampleMatcher MATCHER_ALL = ExampleMatcher.matchingAll()
                .withMatcher("author", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("status", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnorePaths("id", "likedUsers");
        return MATCH_ALL ? MATCHER_ALL:MATCHER_ANY;
    }
}
