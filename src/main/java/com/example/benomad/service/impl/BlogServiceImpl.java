package com.example.benomad.service.impl;

import com.example.benomad.dto.*;
import com.example.benomad.entity.Blog;
import com.example.benomad.entity.Comment;
import com.example.benomad.entity.DeletionInfo;
import com.example.benomad.entity.User;
import com.example.benomad.enums.Content;
import com.example.benomad.enums.ImagePath;
import com.example.benomad.enums.IncludeContent;
import com.example.benomad.enums.ReviewStatus;
import com.example.benomad.exception.ContentIsAlreadyLikedException;
import com.example.benomad.exception.ContentIsNotLikedException;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.exception.NoAccessException;
import com.example.benomad.mapper.BlogMapper;
import com.example.benomad.mapper.DeletionInfoMapper;
import com.example.benomad.repository.BlogRepository;
import com.example.benomad.security.domain.Role;
import com.example.benomad.service.BlogService;
import lombok.RequiredArgsConstructor;
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
    private final DeletionInfoMapper deletionInfoMapper;
    private final ImageServiceImpl imageService;



    @Override
    public BlogDTO getBlogById(Long blogId) {
        return blogMapper.entityToDto(getBlogEntityById(blogId));
    }

    @Override
    public List<BlogDTO> getMyBlogs() {
        User user = userService.getUserEntityById(authService.getCurrentUserId());
        return blogMapper.entityListToDtoList(blogRepository.findByAuthorAndIsDeleted(user, false));
    }

    @Override
    public BlogDTO updateBlogById(Long blogId, BlogDTO blogDTO) {
        checkBlog(blogId);
        Blog blog = blogMapper.dtoToEntity(blogDTO);
        blog.setId(blogId);
        blog.setReviewStatus(ReviewStatus.PENDING);
        blog.setUpdateDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        return blogMapper.entityToDto(blogRepository.save(blog));
    }

    @Override
    public BlogDTO deleteBlogById(Long blogId, DeletionInfoDTO infoDTO) {
        checkBlog(blogId);
        Blog blog = getBlogEntityById(blogId);
        if (blog.getIsDeleted()) {
            throw new ContentNotFoundException(Content.BLOG, "id", String.valueOf(blogId));
        }
        blog.setIsDeleted(true);
        infoDTO = infoDTO != null ? infoDTO :
                new DeletionInfoDTO(null, "Blog was deleted by author", null, null);
        DeletionInfo info = deletionInfoMapper.dtoToEntity(infoDTO);
        info.setDeletionDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        info.setResponsibleUser(userService.getUserEntityById(authService.getCurrentUserId()));
        blog.setDeletionInfo(info);
        return blogMapper.entityToDto(blogRepository.save(blog));
    }

    @Override
    public List<UserDTO> getAuthors(String firstName, String lastName) {
        return userService.getBlogAuthors(firstName, lastName);
    }

    @Override
    public List<BlogDTO> getBlogsByAttributes(String title,
                                              IncludeContent includeContent,
                                              ReviewStatus reviewStatus, boolean MATCH_ALL) {
        Blog blog = Blog.builder()
                .title(title)
                .reviewStatus(reviewStatus)
                .build();
        if (includeContent != IncludeContent.ALL) {
            blog.setIsDeleted(includeContent == IncludeContent.ONLY_DELETED);
        }

        Example<Blog> example = Example.of(blog, getExample(MATCH_ALL, includeContent));

        List<Blog> blogs = blogRepository.findAll(example);

        return blogMapper.entityListToDtoList(blogs);
    }

    @Override
    public MessageResponse likeDislikeBlogById(Long blogId, boolean isDislike) {
        Long userId = authService.getCurrentUserId();
        User user = userService.getUserEntityById(userId);
        Blog blog = getBlogEntityById(blogId);
        Set<User> likedUsers = blog.getLikedUsers();
        boolean isAlreadyLiked = likedUsers.contains(user);
        String message;
        if (isDislike) {
            if (!isAlreadyLiked) {
                throw new ContentIsNotLikedException(Content.BLOG);
            }
            likedUsers.remove(user);
            message = String.format("Like has been successfully removed from blog with id = {%d}!", blogId);
        }else{
            if (isAlreadyLiked) {
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
    public MessageResponse approveBlog(Long blogId) {
        Blog blog = getBlogEntityById(blogId);
        blog.setReviewStatus(ReviewStatus.APPROVED);
        blogRepository.save(blog);
        return new MessageResponse("Blog was successfully approved!", 200);
    }

    @Override
    public MessageResponse rejectBlog(Long blogId) {
        Blog blog = getBlogEntityById(blogId);
        blog.setReviewStatus(ReviewStatus.REJECTED);
        blogRepository.save(blog);
        return new MessageResponse("Blog was successfully rejected!", 200);
    }

    @Override
    public List<BlogDTO> getBlogsByAuthorId(Long userId) {
        return blogMapper.entityListToDtoList(
                blogRepository.findByAuthorAndIsDeleted(userService.getUserEntityById(userId), false));
    }


    @Override
    public MessageResponse insertImagesByBlogId(Long blogId, MultipartFile[] files) {
        Blog blog = getBlogEntityById(blogId);
        blog.setImageUrls(imageService.uploadImages(files, ImagePath.BLOG));
        blogRepository.save(blog);
        return new MessageResponse("Images have been successfully added to the blog!", 200);
    }

    @Override
    public List<String> getImagesById(Long blogId) {
        Blog blog = getBlogEntityById(blogId);
        return blog.getImageUrls();
    }

    @Override
    public BlogDTO insertBlogWithImages(BlogDTO blogDTO, MultipartFile[] files) {
        Blog blog = blogMapper.dtoToEntity(blogDTO);
        blog.setIsDeleted(false);
        blog.setReviewStatus(ReviewStatus.PENDING);
        blog.setImageUrls(imageService.uploadImages(files, ImagePath.BLOG));
        blog.setCreationDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        blog.setAuthor(userService.getUserEntityById(authService.getCurrentUserId()));
        return blogMapper.entityToDto(blogRepository.save(blog));
    }

    @Override
    public MessageResponse insertImages64ByBlogId(Long id, ImageDTO[] files) {
        Blog blog = getBlogEntityById(id);
        blog.setImageUrls(imageService.uploadImages64(files,ImagePath.BLOG));
        blogRepository.save(blog);
        return new MessageResponse("Images have been successfully added to the blog!", 200);
    }

    @Override
    public void addComment(Long blogId, Comment comment) {
        Blog blog = getBlogEntityById(blogId);
        blog.getComments().add(comment);
        blogRepository.save(blog);
    }

    @Override
    public Long insertBlog(BlogDTO blogDTO) {
        Blog blog = blogMapper.dtoToEntity(blogDTO);
        blog.setIsDeleted(false);
        blog.setReviewStatus(ReviewStatus.PENDING);
        blog.setAuthor(userService.getUserEntityById(authService.getCurrentUserId()));
        blog.setCreationDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        return blogRepository.save(blog).getId();
    }

    @Override
    public Blog getBlogEntityById(Long blogId) {
        return blogRepository.findById(blogId).orElseThrow(() -> {
            throw new ContentNotFoundException(Content.BLOG, "id", String.valueOf(blogId));
        });
    }

    private void checkBlog(Long blogId) {
        Blog blog = getBlogEntityById(blogId);
        if (!blog.getAuthor().getId().equals(authService.getCurrentUserId())
                && !blog.getAuthor().getRoles().contains(Role.ROLE_ADMIN)
                && !blog.getAuthor().getRoles().contains(Role.ROLE_SUPERADMIN)) {
            throw new NoAccessException();
        }
    }

    private ExampleMatcher getExample(boolean MATCH_ALL, IncludeContent includeContent) {
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

        if (includeContent == IncludeContent.ALL) {
            return MATCH_ALL ? MATCHER_ALL_WITHOUT_DELETED : MATCHER_ANY_WITHOUT_DELETED;
        } else {
            return MATCH_ALL ? MATCHER_ALL_WITH_DELETED : MATCHER_ANY_WITH_DELETED;
        }
    }
}
