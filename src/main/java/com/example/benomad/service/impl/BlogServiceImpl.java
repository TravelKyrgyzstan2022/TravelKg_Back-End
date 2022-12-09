package com.example.benomad.service.impl;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.dto.DeletionInfoDTO;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.Blog;
import com.example.benomad.entity.Comment;
import com.example.benomad.entity.Place;
import com.example.benomad.entity.User;
import com.example.benomad.enums.Content;
import com.example.benomad.enums.ImagePath;
import com.example.benomad.enums.IncludeContent;
import com.example.benomad.enums.ReviewStatus;
import com.example.benomad.exception.*;
import com.example.benomad.mapper.BlogMapper;
import com.example.benomad.mapper.DeletionInfoMapper;
import com.example.benomad.mapper.UserMapper;
import com.example.benomad.repository.BlogRepository;
import com.example.benomad.service.BlogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Transactional
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final UserServiceImpl userService;
    private final AuthServiceImpl authService;
    private final BlogMapper blogMapper;
    private final UserMapper userMapper;
    private final DeletionInfoMapper deletionInfoMapper;
    private final ImageServiceImpl imageService;

    @Override
    public BlogDTO insertBlog(BlogDTO blogDTO) throws ContentNotFoundException {
        blogDTO.setId(null);
        blogDTO.setIsDeleted(false);
        Blog blog = blogMapper.dtoToEntity(blogDTO);
        blog.setAuthor(userService.getUserEntityById(authService.getCurrentUserId()));
        blog.setCreationDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        blogDTO.setId(blog.getId());
        return blogDTO;
    }

    @Override
    public BlogDTO getBlogById(Long blogId) throws ContentNotFoundException {
        return blogMapper.entityToDto(getBlogEntityById(blogId));
    }

    @Override
    public List<BlogDTO> getMyBlogs() {
        User user = userService.getUserEntityById(authService.getCurrentUserId());
        return blogMapper.entityListToDtoList(blogRepository.findByAuthor(user));
    }

    @Override
    public BlogDTO updateMyBlogById(BlogDTO blogDTO, Long blogId) {
        checkBlog(blogId);
        blogDTO.setId(blogId);
        Blog blog = blogMapper.dtoToEntity(blogDTO);
        blog.setUpdateDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        blogRepository.save(blog);
        return blogMapper.entityToDto(blog);
    }

    private void checkBlog(Long blogId){
        Blog blog = getBlogEntityById(blogId);
        if(!blog.getAuthor().getId().equals(authService.getCurrentUserId())){
            throw new NoAccessException();
        }
    }

    @Override
    public BlogDTO deleteMyBlogById(Long blogId) {
        checkBlog(blogId);
        Blog blog = getBlogEntityById(blogId);
        blog.setIsDeleted(true);
        return blogMapper.entityToDto(blogRepository.save(blog));
    }

    public List<UserDTO> getAuthors(String firstName, String lastName){
        return userService.getBlogAuthors(firstName, lastName);
    }

    @Override
    public List<BlogDTO> getBlogsByAttributes(String title,
                                              IncludeContent includeContent,
                                              ReviewStatus reviewStatus, boolean MATCH_ALL){
        Blog blog = Blog.builder()
                .title(title)
                .reviewStatus(reviewStatus)
                .build();
        if(includeContent != IncludeContent.ALL){
            blog.setIsDeleted(includeContent == IncludeContent.ONLY_DELETED);
        }

        Example<Blog> example = Example.of(blog, getExample(MATCH_ALL, includeContent));

        List<Blog> blogs = blogRepository.findAll(example);

        return blogMapper.entityListToDtoList(blogs);
    }



    @Override
    public MessageResponse likeDislikeBlogById(Long blogId, boolean isDislike){
        Long userId = authService.getCurrentUserId();
        User user = userService.getUserEntityById(userId);
        Blog blog = getBlogEntityById(blogId);
        Set<User> likedUsers = blog.getLikedUsers();
        boolean isAlreadyLiked = likedUsers.contains(user);
        String message;
        if(isDislike){
            if(!isAlreadyLiked){
                throw new ContentIsNotLikedException(Content.BLOG);
            }
            likedUsers.remove(user);
            message = String.format("Like has been successfully removed from blog with id = {%d}!", blogId);
        }else{
            if(isAlreadyLiked){
                throw new ContentIsAlreadyLikedException(Content.BLOG);
            }
            likedUsers.add(user);
            message = String.format("Like has been successfully added to the blog with id = {%d}!", blogId);
        }
        blog.setLikedUsers(likedUsers);
        blogRepository.save(blog);
        return new MessageResponse(message, 200);
    }

    @Override
    public BlogDTO updateBlogById(Long blogId, BlogDTO blogDTO){
        getBlogEntityById(blogId);
        blogDTO.setId(blogId);
        Blog blog = blogMapper.dtoToEntity(blogDTO);
        blog.setCreationDate(null);
        blog.setUpdateDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        blogRepository.save(blog);
        return blogDTO;
    }

    @Override
    public BlogDTO deleteBlogById(Long blogId, DeletionInfoDTO infoDTO){
        Blog blog = getBlogEntityById(blogId);
        blog.setIsDeleted(true);
        infoDTO.setDeletionDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        blog.setDeletionInfo(deletionInfoMapper.dtoToEntity(infoDTO));
        blogRepository.save(blog);
        return blogMapper.entityToDto(blog);
    }

    // FIXME: 09.12.2022
    @Override
    public MessageResponse approveBlog(Long blogId) {
        return null;
    }

    @Override
    public MessageResponse rejectBlog(Long blogId) {
        return null;
    }

    @Override
    public List<BlogDTO> getBlogsByAuthorId(Long userId) {
        return blogMapper.entityListToDtoList(blogRepository.findByAuthor(userService.getUserEntityById(userId)));
    }

    @Override
    public MessageResponse insertImagesByBlogId(Long blogId, MultipartFile[] files){
        Blog blog = getBlogEntityById(blogId);
        blog.setImageUrls(imageService.uploadImages(files, ImagePath.BLOG));
        blogRepository.save(blog);
        return new MessageResponse("Images have been successfully added to the blog!", 200);
    }

    @Override
    public List<String> getImagesById(Long blogId){
        Blog blog = getBlogEntityById(blogId);
        return blog.getImageUrls();
    }

    @Override
    public BlogDTO insertMyBlogWithImages(BlogDTO blogDTO, MultipartFile[] files) {
        blogDTO.setId(null);
        blogDTO.setIsDeleted(false);
        blogDTO.setImageUrls(imageService.uploadImages(files, ImagePath.BLOG));
        Blog blog = blogMapper.dtoToEntity(blogDTO);
        blog.setCreationDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        blog.setAuthor(userService.getUserEntityById(authService.getCurrentUserId()));
        return blogMapper.entityToDto(blogRepository.save(blog));
    }

    public void addComment(Long blogId, Comment comment){
        Blog blog = getBlogEntityById(blogId);
        Set<Comment> comments = blog.getComments();
        comments.add(comment);
        blog.setComments(comments);
        blogRepository.save(blog);
    }

    public Blog getBlogEntityById(Long blogId){
        return blogRepository.findById(blogId).orElseThrow(() -> {
            throw new ContentNotFoundException(Content.BLOG, "id", String.valueOf(blogId));
        });
    }

    private ExampleMatcher getExample(boolean MATCH_ALL, IncludeContent includeContent){
        ExampleMatcher MATCHER_ANY_WITH_DELETED = ExampleMatcher.matchingAny()
                .withMatcher("author", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("status", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("isDeleted", ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnorePaths("id", "likedUsers");
        ExampleMatcher MATCHER_ALL_WITH_DELETED = ExampleMatcher.matchingAll()
                .withMatcher("author", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("status", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("isDeleted", ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnorePaths("id", "likedUsers");
        ExampleMatcher MATCHER_ANY_WITHOUT_DELETED = ExampleMatcher.matchingAny()
                .withMatcher("author", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("status", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("isDeleted", ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnorePaths("id", "likedUsers", "isDeleted");
        ExampleMatcher MATCHER_ALL_WITHOUT_DELETED = ExampleMatcher.matchingAll()
                .withMatcher("author", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("status", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnorePaths("id", "likedUsers", "isDeleted");

        if(includeContent == IncludeContent.ALL){
            return MATCH_ALL ? MATCHER_ALL_WITHOUT_DELETED : MATCHER_ANY_WITHOUT_DELETED;
        }else{
            return MATCH_ALL ? MATCHER_ALL_WITH_DELETED : MATCHER_ANY_WITH_DELETED;
        }
    }
}
