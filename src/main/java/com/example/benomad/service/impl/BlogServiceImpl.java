package com.example.benomad.service.impl;

import com.example.benomad.dto.BlogDTO;
import com.example.benomad.dto.DeletionInfoDTO;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.Blog;
import com.example.benomad.entity.User;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.enums.ImagePath;
import com.example.benomad.enums.IncludeContent;
import com.example.benomad.enums.ReviewStatus;
import com.example.benomad.exception.*;
import com.example.benomad.logger.LogWriterServiceImpl;
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

import java.io.IOException;
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
    private final UserServiceImpl userService;
    private final AuthServiceImpl authService;
    private final BlogMapper blogMapper;
    private final UserMapper userMapper;
    private final DeletionInfoMapper deletionInfoMapper;
    private final ImageServiceImpl imageService;
    private final LogWriterServiceImpl logWriter;

    @Override
    public BlogDTO insertBlog(BlogDTO blogDTO) throws ContentNotFoundException {
        blogDTO.setId(null);
        blogDTO.setAuthor(userMapper.entityToDto(
                userService.getUserEntityById(authService.getCurrentUserId())));
        blogDTO.setId(blogRepository.save(blogMapper.dtoToEntity(blogDTO)).getId());
        logWriter.insert(String.format("%s - Inserted blog with id = %d", authService.getCurrentEmail(), blogDTO.getId()));
        return blogDTO;
    }

    public BlogDTO insertMyBlog(BlogDTO blogDTO){
        checkUserActivation();
        blogDTO.setId(null);
        blogDTO.setAuthor(userMapper.entityToDto(
                userService.getUserEntityById(authService.getCurrentUserId())));
        blogDTO.setCreationDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        Blog blog = blogRepository.save(blogMapper.dtoToEntity(blogDTO));
        blogDTO.setId(blog.getId());
        return blogDTO;
    }

    @Override
    public BlogDTO getBlogById(Long blogId) throws ContentNotFoundException {
        BlogDTO blogDTO = blogMapper.entityToDto(getBlogEntityById(blogId));
        logWriter.get(String.format("%s - Returned blog with id = %d", authService.getCurrentEmail(), blogId));
        return blogDTO;
    }

    @Override
    public List<BlogDTO> getMyBlogs() {
        User user = userService.getUserEntityById(authService.getCurrentUserId());
        return blogMapper.entityListToDtoList(blogRepository.findByAuthor(user));
    }

    @Override
    public BlogDTO updateMyBlogById(BlogDTO blogDTO, Long blogId) {
        checkUserActivation();
        checkBlog(blogId);
        blogDTO.setId(blogId);
        blogRepository.save(blogMapper.dtoToEntity(blogDTO));
        return blogDTO;
    }

    private void checkUserActivation(){
        Long userId = authService.getCurrentUserId();
        User user = userService.getUserEntityById(userId);
        if(!user.isActivated()){
            throw new UserNotActivatedException();
        }
    }
    private void checkBlog(Long blogId){
        Blog blog = getBlogEntityById(blogId);
        if(!blog.getAuthor().getId().equals(authService.getCurrentUserId())){
            throw new NoAccessException();
        }
    }

    @Override
    public BlogDTO deleteMyBlogById(Long blogId) {
        checkUserActivation();
        checkBlog(blogId);
        Blog blog = getBlogEntityById(blogId);
        blogRepository.delete(blog);
        return blogMapper.entityToDto(blog);
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
            blog.setDeleted(includeContent == IncludeContent.ONLY_DELETED);
        }

        Example<Blog> example = Example.of(blog, getExample(MATCH_ALL));

        List<Blog> blogs = blogRepository.findAll(example);

        logWriter.get(String.format("%s - Returned %d blogs", authService.getCurrentEmail(), blogs.size()));

        return blogMapper.entityListToDtoList(blogs);
    }



    @Override
    public BlogDTO likeDislikeBlogById(Long blogId, boolean isDislike){
        Long userId = authService.getCurrentUserId();

        Blog blog = getBlogEntityById(blogId);
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
        logWriter.update(String.format("%s - %s blog with id = %d", authService.getCurrentEmail(),
                isDislike ? "Disliked" : "Liked", blogId));
        return blogMapper.entityToDto(blog);
    }

    @Override
    public BlogDTO updateBlogById(Long blogId, BlogDTO blogDTO){
        if(!blogRepository.existsById(blogId)){
            throw new ContentNotFoundException(ContentNotFoundEnum.BLOG, "id" , String.valueOf(blogId));
        }
        blogDTO.setId(blogId);
        Blog blog = blogMapper.dtoToEntity(blogDTO);
        blog.setCreationDate(null);
        blog.setUpdateDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        blogRepository.save(blog);
        addIsLikedAndLikesCount(blogDTO, authService.getCurrentUserId());
        logWriter.update(String.format("%s - Updated blog with id = %d", authService.getCurrentEmail(), blogId));
        return blogDTO;
    }

    @Override
    public BlogDTO deleteBlogById(Long blogId, DeletionInfoDTO infoDTO){
        Blog blog = getBlogEntityById(blogId);
        blog.setDeleted(true);
        infoDTO.setDeletionDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        blog.setDeletionInfo(deletionInfoMapper.dtoToEntity(infoDTO));
        blogRepository.save(blog);
        logWriter.delete(String.format("%s - Deleted blog with id = %d", authService.getCurrentEmail(), blogId));
        return blogMapper.entityToDto(blog);
    }

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
    public boolean insertImagesByBlogId(Long blogId, MultipartFile[] files){
        Blog blog = getBlogEntityById(blogId);
        blog.setImageUrls(imageService.uploadImages(files, ImagePath.BLOG));
        blogRepository.save(blog);
        return true;
    }

    @Override
    public List<String> getImagesById(Long blogId){
        Blog blog = getBlogEntityById(blogId);
        return blog.getImageUrls();
    }

    @Override
    @Transactional
    public boolean insertMyBlogWithImages(BlogDTO blogDTO, MultipartFile[] files) {
        checkUserActivation();
        blogDTO.setId(null);
        blogDTO.setAuthor(userMapper.entityToDto(userService.getUserEntityById(authService.getCurrentUserId())));
        blogDTO.setCreationDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        blogDTO.setImageUrls(imageService.uploadImages(files, ImagePath.BLOG));
        blogRepository.save(blogMapper.dtoToEntity(blogDTO));
        return true;
    }

    public Blog getBlogEntityById(Long blogId){
        return blogRepository.findById(blogId).orElseThrow(() -> {
            throw new ContentNotFoundException(ContentNotFoundEnum.BLOG, "id", String.valueOf(blogId));
        });
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
                .withMatcher("isDeleted", ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnorePaths("id", "likedUsers");
        ExampleMatcher MATCHER_ALL = ExampleMatcher.matchingAll()
                .withMatcher("author", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("status", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("isDeleted", ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnorePaths("id", "likedUsers");
        return MATCH_ALL ? MATCHER_ALL:MATCHER_ANY;
    }
}
