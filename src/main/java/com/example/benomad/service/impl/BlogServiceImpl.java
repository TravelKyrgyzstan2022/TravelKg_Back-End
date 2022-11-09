package com.example.benomad.service.impl;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.entity.Blog;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.enums.ReviewStatus;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.exception.ContentIsAlreadyLikedException;
import com.example.benomad.exception.ContentIsNotLikedException;
import com.example.benomad.mapper.BlogMapper;
import com.example.benomad.repository.BlogRepository;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final BlogMapper blogMapper;

    @Override
    public BlogDTO insertBlog(BlogDTO blogDTO) throws ContentNotFoundException {
        blogDTO.setId(null);
        return blogMapper.entityToDto(blogRepository.save(
                        blogMapper.dtoToEntity(blogDTO)), null);
    }

    @Override
    public BlogDTO getBlogById(Long blogId, Principal principal) throws ContentNotFoundException {
        Long userId = null;
        if(principal != null){
            userId = userRepository.findByEmail(principal.getName()).getId();
        }
        return blogMapper.entityToDto(
                blogRepository.findById(blogId).orElseThrow(
                        () -> {
                            throw new ContentNotFoundException(ContentNotFoundEnum.BLOG, blogId);
                        }),
                userId);
    }

    @Override
    public List<BlogDTO> getBlogsByAttributes(Long authorId, Principal principal,
                                              String title, ReviewStatus reviewStatus, boolean MATCH_ALL)
            throws ContentNotFoundException {
        Long userId = null;
        if(principal != null){
            userId = userRepository.findByEmail(principal.getName()).getId();
        }

        Blog blog = Blog.builder()
                .title(title)
                .reviewStatus(reviewStatus)
                .build();

        if(authorId != null){
            blog.setAuthor(
                    userRepository.findById(authorId).orElseThrow(
                            () -> {
                                throw new ContentNotFoundException(ContentNotFoundEnum.USER, authorId);
                            })
            );
        }

        Example<Blog> example = Example.of(blog, getExample(MATCH_ALL));

        List<Blog> blogs = blogRepository.findAll(example);

        return blogMapper.entityListToDtoList(blogs, userId);
    }


    @Override
    public BlogDTO likeDislikeBlogById(Long blogId, Principal principal, boolean isDislike) throws ContentNotFoundException {
        Long userId = null;
        if(principal != null){
            userId = userRepository.findByEmail(principal.getName()).getId();
            if(!userRepository.existsById(userId)){
                throw new ContentNotFoundException(ContentNotFoundEnum.USER, userId);
            }
        }
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> {
            throw new ContentNotFoundException(ContentNotFoundEnum.BLOG, blogId);
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
