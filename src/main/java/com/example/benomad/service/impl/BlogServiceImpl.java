package com.example.benomad.service.impl;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.entity.Blog;
import com.example.benomad.enums.AwsBucket;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.enums.ImagePath;
import com.example.benomad.enums.Status;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.exception.ContentIsAlreadyLikedException;
import com.example.benomad.exception.ContentIsNotLikedException;
import com.example.benomad.exception.FailedWhileUploadingException;
import com.example.benomad.mapper.BlogMapper;
import com.example.benomad.repository.BlogRepository;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final BlogMapper blogMapper;
    private final ImageServiceImpl imageService;

    @Override
    public BlogDTO insertBlog(BlogDTO blogDTO) throws ContentNotFoundException {
        blogDTO.setId(null);
        return blogMapper.entityToDto(blogRepository.save(
                        blogMapper.dtoToEntity(blogDTO)), null);
    }

    @Override
    public BlogDTO getBlogById(Long blogId, Long currentUserId) throws ContentNotFoundException {
        return blogMapper.entityToDto(
                blogRepository.findById(blogId).orElseThrow(
                        () -> {
                            throw new ContentNotFoundException(ContentNotFoundEnum.BLOG, blogId);
                        }),
                currentUserId);
    }

    @Override
    public List<BlogDTO> getBlogsByAttributes(Long authorId, Long currentUserId,
                                              String title, Status status, boolean MATCH_ALL)
            throws ContentNotFoundException {
        Blog blog = Blog.builder()
                .title(title)
                .author(
                        userRepository.findById(authorId).orElseThrow(
                                () -> {
                                    throw new ContentNotFoundException(ContentNotFoundEnum.USER, authorId);
                                })
                )
                .status(status)
                .build();

        Example<Blog> example = Example.of(blog, getExample(MATCH_ALL));

        List<Blog> blogs = blogRepository.findAll(example);

        return blogMapper.entityListToDtoList(blogs, currentUserId);
    }


    @Override
    public BlogDTO likeDislikeBlogById(Long blogId, Long userId, boolean isDislike) throws ContentNotFoundException {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> {
            throw new ContentNotFoundException(ContentNotFoundEnum.BLOG, blogId);
        });
        if(!userRepository.existsById(userId)){
            throw new ContentNotFoundException(ContentNotFoundEnum.USER, userId);
        }
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
        return blogMapper.entityToDto(blog, userId);
    }

    @Override
    public BlogDTO updateBlogById(Long blogId, BlogDTO blogDTO) throws ContentNotFoundException {
        if(!blogRepository.existsById(blogId)){
            throw new ContentNotFoundException(ContentNotFoundEnum.BLOG, blogId);
        }
        blogDTO.setId(blogId);
        blogRepository.save(blogMapper.dtoToEntity(blogDTO));
        addIsLikedAndLikesCount(blogDTO, null);
        return blogDTO;
    }

    @Override
    public BlogDTO deleteBlogById(Long blogId) throws ContentNotFoundException {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> {
            throw new ContentNotFoundException(ContentNotFoundEnum.BLOG, blogId);
        });
        blogRepository.delete(blog);
        return blogMapper.entityToDto(blog, null);
    }

    @Override
    public Long insertImageByBlogId(Long id, MultipartFile file) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException(ContentNotFoundEnum.BLOG,id));
        imageService.checkIsNotEmpty(file);
        imageService.checkIsImage(file);
        Map<String, String> metadata = imageService.getMetaData(file);
        String pathToFile = String.format("%s/%s", AwsBucket.MAIN_BUCKET.getBucketName(),ImagePath.BLOG.getPathToImage());
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
                .orElseThrow(() -> new ContentNotFoundException(ContentNotFoundEnum.PLACE,id));
        String pathToFile = String.format("%s/%s", AwsBucket.MAIN_BUCKET.getBucketName(), ImagePath.BLOG.getPathToImage());
        return blog.getImageUrl().map(x -> imageService.getAwsImageByPathAndKey(pathToFile,x)).orElse(new byte[0]);
    }

    public boolean checkBlogForLikeByIdWithoutException(Long blogId, Long userId){
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
