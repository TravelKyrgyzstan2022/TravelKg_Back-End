package com.example.benomad.service.impl;

import com.example.benomad.dto.DeletionInfoDTO;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.entity.User;

import com.example.benomad.enums.Content;
import com.example.benomad.enums.ImagePath;
import com.example.benomad.enums.IncludeContent;
import com.example.benomad.exception.UserAttributeTakenException;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.mapper.DeletionInfoMapper;
import com.example.benomad.security.domain.Role;
import com.example.benomad.security.domain.UserDetailsImpl;
import com.example.benomad.util.JwtUtils;
import com.example.benomad.dto.UserDTO;

import com.example.benomad.mapper.UserMapper;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private DeletionInfoMapper deletionInfoMapper;
    private JwtUtils jwtUtils;
    private PasswordEncoder encoder;
    private AuthServiceImpl authService;
    private ImageServiceImpl imageService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getUserEntityByEmail(email);
        return UserDetailsImpl.build(user);
    }

    @Override
    public String getUserAuthenticationToken(User user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            return jwtUtils.generateTokenFromEmail(user.getEmail());
        } else {
            return null;
        }
    }

    @Override
    public UserDTO getUserById(Long userId) throws ContentNotFoundException {
        return userMapper.entityToDto(getUserEntityById(userId));
    }

    //is not used
    @Override
    public UserDTO insertUser(UserDTO userDTO) throws UserAttributeTakenException {
        userDTO.setId(null);
        userDTO.setIsDeleted(false);
        if(userRepository.existsByEmail(userDTO.getEmail())){
            throw new UserAttributeTakenException("email: ('" + userDTO.getEmail() + "')");
        }
        User user = userMapper.dtoToEntity(userDTO);
        user.setRegistrationDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        userDTO.setId(userRepository.save(user).getId());
        return userDTO;
    }

    public void setActivated(String email){
        User user = getUserEntityByEmail(email);
        user.setIsActivated(true);
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> getUsersByAttributes(String firstName, String lastName, IncludeContent includeContent,
                                              String email, String phoneNumber, boolean MATCH_ALL) {
        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
        if(includeContent != IncludeContent.ALL){
            user.setIsDeleted(includeContent == IncludeContent.ONLY_DELETED);
        }
        Example<User> example = Example.of(user, getExample(MATCH_ALL, includeContent));
        return userMapper.entityListToDtoList(userRepository.findAll(example, Sort.by(Sort.Direction.ASC, "id")));
    }

    @Override
    public List<UserDTO> getBlogAuthors(String firstName, String lastName) {
        List<User> authors;
        if(firstName != null && lastName != null){
            authors = userRepository.findBlogAuthorsByFirstNameAndLastName(firstName, lastName);
        }else if(firstName != null){
            authors = userRepository.findBlogAuthorsByFirstName(firstName);
        }else if(lastName != null){
            authors = userRepository.findBlogAuthorsByLastName(lastName);
        }else{
            authors = userRepository.findBlogAuthors();
        }
        return userMapper.entityListToDtoList(authors);
    }

    @Override
    public UserDTO getUserByEmail(String email){
        return userMapper.entityToDto(getUserEntityByEmail(email));
    }

    @Override
    public UserDTO getCurrentUser() {
        return userMapper.entityToDto(getUserEntityByEmail(authService.getCurrentEmail()));
    }

    @Override
    public UserDTO updateCurrentUser(UserDTO userDTO) {
        userDTO.setEmail(authService.getCurrentEmail());
        userDTO.setId(getUserEntityByEmail(authService.getCurrentEmail()).getId());
        return userMapper.entityToDto(userRepository.save(userMapper.dtoToEntity(userDTO)));
    }

    @Override
    public UserDTO updateUserById(Long userId, UserDTO userDTO){
        User user = getUserEntityById(userId);
        boolean isAdmin = user.getRoles().contains(Role.ROLE_ADMIN);
        if(!user.getEmail().equals(userDTO.getEmail())){
            Example<User> example = Example.of(user, getExampleForAttribute("email"));
            if(userRepository.findAll(example).size() > 0){
                throw new UserAttributeTakenException("email: (CHANGING EMAIL IS NOT ALLOWED)");
            }
        }
        if(!Objects.equals(user.getPhoneNumber(), userDTO.getPhoneNumber())){
            Example<User> example = Example.of(user, getExampleForAttribute("phoneNumber"));
            if(userRepository.findAll(example).size() > 0){
                throw new UserAttributeTakenException("phone_number: ('" + user.getPhoneNumber() + "')");
            }
        }
        if(!isAdmin || authService.getCurrentEmail().equals(userDTO.getEmail())){
            userDTO.setId(userId);
            userRepository.save(userMapper.dtoToEntity(userDTO));
        }
        return userDTO;
    }

    @Override
    public UserDTO deleteUserById(Long userId, DeletionInfoDTO infoDTO) throws ContentNotFoundException {
        User user = getUserEntityById(userId);
        boolean isAdmin = user.getRoles().contains(Role.ROLE_ADMIN) || user.getRoles().contains(Role.ROLE_SUPERADMIN);
        if(!isAdmin){
            user.setIsDeleted(true);
            infoDTO.setDeletionDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
            user.setDeletionInfo(deletionInfoMapper.dtoToEntity(infoDTO));
            userRepository.save(user);
        }
        return userMapper.entityToDto(user);
    }

    @Override
    public MessageResponse insertMyImage(MultipartFile file) {
        if (authService.getCurrentUserId() != null  && userRepository.findById(authService.getCurrentUserId()).isPresent()) {
            User user = userRepository.findById(authService.getCurrentUserId()).get();
            user.setImageUrl(imageService.uploadImage(file, ImagePath.USER));
            userRepository.save(user);
        }
        return new MessageResponse("Image has been successfully set as a profile picture!", 200);
    }

    public void resetPassword(String email, String password){
        User user = getUserEntityByEmail(email);
        user.setPassword(encoder.encode(password));
        userRepository.save(user);
    }

    public User getUserEntityById(Long userId){
        return userRepository.findById(userId).orElseThrow(
                () -> new ContentNotFoundException(Content.USER, "id", String.valueOf(userId))
        );
    }

    public User getUserEntityByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(
                () -> new ContentNotFoundException(Content.USER, "email", email)
        );
    }

    private ExampleMatcher getExample(boolean MATCH_ALL, IncludeContent includeContent){
        ExampleMatcher MATCHER_ANY_WITH_DELETED = ExampleMatcher.matchingAny()
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.exact().ignoreCase())
                .withMatcher("phoneNumber", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("isDeleted", ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnorePaths("id", "password", "roles", "places", "blogs", "active");
        ExampleMatcher MATCHER_ALL_WITH_DELETED = ExampleMatcher.matchingAll()
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.exact().ignoreCase())
                .withMatcher("phoneNumber", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("isDeleted", ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnorePaths("id", "password", "roles", "places", "blogs", "active");
        ExampleMatcher MATCHER_ANY_WITHOUT_DELETED = ExampleMatcher.matchingAny()
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.exact().ignoreCase())
                .withMatcher("phoneNumber", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnorePaths("id", "password", "roles", "places", "blogs", "active", "isDeleted");
        ExampleMatcher MATCHER_ALL_WITHOUT_DELETED = ExampleMatcher.matchingAll()
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.exact().ignoreCase())
                .withMatcher("phoneNumber", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnorePaths("id", "password", "roles", "places", "blogs", "active", "isDeleted");

        if(includeContent == IncludeContent.ALL){
            return MATCH_ALL ? MATCHER_ALL_WITHOUT_DELETED : MATCHER_ANY_WITHOUT_DELETED;
        }else{
            return MATCH_ALL ? MATCHER_ALL_WITH_DELETED : MATCHER_ANY_WITH_DELETED;
        }

    }

    private ExampleMatcher getExampleForAttribute(String attribute){
        if(attribute.equals("email")){
            return ExampleMatcher.matchingAny()
                    .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withIgnorePaths("id", "password", "roles", "places", "blogs", "firstName", "lastName",
                            "active", "activationCode", "phoneNumber", "isDeleted");
        }
        return ExampleMatcher.matchingAny()
                .withMatcher("phoneNumber", ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnorePaths("id", "password", "roles", "places", "blogs", "firstName", "lastName",
                        "active", "activationCode", "email", "isDeleted");
    }

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, DeletionInfoMapper deletionInfoMapper, JwtUtils jwtUtils, PasswordEncoder encoder, @Lazy AuthServiceImpl authService, ImageServiceImpl imageService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.deletionInfoMapper = deletionInfoMapper;
        this.jwtUtils = jwtUtils;
        this.encoder = encoder;
        this.authService = authService;
        this.imageService = imageService;
    }


}
