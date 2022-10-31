package com.example.benomad.service.impl;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.entity.Blog;
import com.example.benomad.entity.User;
import com.example.benomad.enums.Status;
import com.example.benomad.exception.BlogNotFoundException;
import com.example.benomad.exception.UserNotFoundException;
import com.example.benomad.mapper.BlogMapper;
import com.example.benomad.repository.BlogRepository;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @Override
    public BlogDTO insertBlog(BlogDTO blogDTO) throws UserNotFoundException{
        blogDTO.setId(null);
        return BlogMapper.entityToDto(blogRepository.save(
                        BlogMapper.dtoToEntity(blogDTO, userRepository)), null, blogRepository);
    }

    @Override
    public BlogDTO getBlogById(Long blogId, Long currentUserId) throws BlogNotFoundException {
        return BlogMapper.entityToDto(blogRepository.findById(blogId)
                .orElseThrow(BlogNotFoundException::new), currentUserId, blogRepository);
    }

    @Override
    public List<BlogDTO> getBlogsByAttributes(Long authorId, Long currentUserId,
                                              String title, Status status, boolean MATCH_ALL)
            throws BlogNotFoundException {
        Blog blog = Blog.builder()
                .title(title)
                .author(userRepository.findById(authorId).orElseThrow(BlogNotFoundException::new))
                .status(status)
                .build();

        Example<Blog> example = Example.of(blog, getExample(MATCH_ALL));

        List<Blog> blogs = blogRepository.findAll(example);

        return BlogMapper.entityListToDtoList(blogs, currentUserId, blogRepository);
    }


    @Override
    public void likeDislikeBlogById(Long blogId, Long userId, boolean isDislike) throws BlogNotFoundException {
        Blog blog = blogRepository.findById(blogId).orElseThrow(BlogNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if(isDislike){
            blogRepository.dislikeBlogById(blogId, userId);
        }else{
            blogRepository.likeBlogById(blogId, userId);
        }
    }

    @Override
    public BlogDTO updateBlogById(BlogDTO blogDTO) throws BlogNotFoundException, UserNotFoundException {
        Blog check = blogRepository.findById(blogDTO.getId()).orElseThrow(BlogNotFoundException::new);
        blogRepository.save(BlogMapper.dtoToEntity(blogDTO, userRepository));
        addIsLikedAndLikesCount(blogDTO, null);
        return blogDTO;
    }

    @Override
    public BlogDTO deleteBlogById(Long id) throws BlogNotFoundException {
        Blog blog = blogRepository.findById(id).orElseThrow(BlogNotFoundException::new);
        blogRepository.delete(blog);
        return BlogMapper.entityToDto(blog, null, blogRepository);
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
