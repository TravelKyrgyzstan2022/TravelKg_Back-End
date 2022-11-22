package com.example.benomad.service.impl;

import com.example.benomad.dto.DeletionInfoDTO;
import com.example.benomad.entity.User;

import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.exception.UserAttributeTakenException;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.logger.LogWriter;
import com.example.benomad.mapper.DeletionInfoMapper;
import com.example.benomad.security.domain.Role;
import com.example.benomad.security.domain.UserDetailsImpl;
import com.example.benomad.security.jwt.JwtUtils;
import com.example.benomad.dto.UserDTO;

import com.example.benomad.mapper.UserMapper;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {


    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final DeletionInfoMapper deletionInfoMapper;
    private final JwtUtils jwtUtils;
    private final BCryptPasswordEncoder passwordEncoder;

    private final MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> {throw new ContentNotFoundException(ContentNotFoundEnum.USER, "email", email);}
        );
        return UserDetailsImpl.build(user);
    }

    @Override
    public String getUserAuthenticationToken(UserDetailsImpl userDetails) {
        if(userRepository.existsByEmail(userDetails.getUsername())) {
            return jwtUtils.generateTokenFromEmail(userDetails.getUsername());
        } else {
            return null;
        }
    }

    @Override
    public String getUserAuthenticationToken(User user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            return jwtUtils.generateTokenFromEmail(user.getEmail());
        } else {
            return null;
        }
    }


    public UserDTO addUser(UserDTO userDTO) {

        User user = userMapper.dtoToEntity(userDTO);

        if(userRepository.existsByEmail(userDTO.getEmail())){
            throw new UserAttributeTakenException("email: ('" + user.getEmail() + "')");
        }

        user.setId(null);
        user.setActivated(true);
        user.setRegistrationDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        LogWriter.auth(String.format("%s - Registration completed", userDTO.getEmail()));
        return userMapper.entityToDto(user);
    }

    @Override
    public UserDTO getUserById(Long userId) throws ContentNotFoundException {
        UserDTO userDTO = userMapper.entityToDto(userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.USER, "id", String.valueOf(userId));
                }
        ));
        LogWriter.get(String.format("%s - Returned user with id = %d", getAuthName(), userId));
        return userDTO;
    }

    //is not used
    @Override
    public UserDTO insertUser(UserDTO userDTO) throws UserAttributeTakenException {
        userDTO.setId(null);
        if(userRepository.existsByEmail(userDTO.getEmail())){
            throw new UserAttributeTakenException("email: ('" + userDTO.getEmail() + "')");
        }
        userDTO.setId(userRepository.save(userMapper.dtoToEntity(userDTO)).getId());
        LogWriter.insert(String.format("%s - Inserted user with id = %d", getAuthName(), userDTO.getId()));
        return userDTO;
    }

    //fixme
    public void setDeleted(){}
    public void setActivated(){}
    public void sameGoesForBlogsBro(){}

    @Override
    public List<UserDTO> getUsersByAttributes(String firstName, String lastName,
                                              String email, String phoneNumber, boolean MATCH_ALL) {
        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
        Example<User> example = Example.of(user, getExample(MATCH_ALL));
        List<UserDTO> userDTOS = userMapper.entityListToDtoList(userRepository.findAll(example, Sort.by(Sort.Direction.ASC, "id")));
        LogWriter.get(String.format("%s - Returned %d users", getAuthName(), userDTOS.size()));
        return userDTOS;
    }

    @Override
    public UserDTO getUserByEmail(String email) throws ContentNotFoundException {
        return null;
    }

    @Override
    public UserDTO getUserByPhoneNumber(String phoneNumber) throws ContentNotFoundException {
        return null;
    }

    @Override
    public UserDTO updateUserById(Long userId, UserDTO userDTO) throws ContentNotFoundException {
        User user =  userRepository.findById(userId).orElseThrow(
                () -> new ContentNotFoundException(ContentNotFoundEnum.USER, "id", String.valueOf(userId))
        );
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
        if(!isAdmin || getAuthName().equals(userDTO.getEmail())){
            userDTO.setId(userId);
            userRepository.save(userMapper.dtoToEntity(userDTO));
        }
        LogWriter.update(String.format("%s - Updated user with id - %d", getAuthName(), userId));
        return userDTO;
    }

    @Override
    //fixme
    public UserDTO deleteUserById(Long userId, DeletionInfoDTO infoDTO) throws ContentNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.USER, "id", String.valueOf(userId));
                });
        boolean isAdmin = !user.getRoles().contains(Role.ROLE_ADMIN);
        if(!isAdmin){
            user.setDeleted(true);
            infoDTO.setDeletionDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
            user.setDeletionInfo(deletionInfoMapper.dtoToEntity(infoDTO));
            userRepository.save(user);
        }
        LogWriter.delete(String.format("%s - %s with id - %d", getAuthName(),
                isAdmin ? "Couldn't delete admin" : "Deleted user", userId));
        return userMapper.entityToDto(user);
    }

    private String getAuthName(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private ExampleMatcher getExample(boolean MATCH_ALL){
        ExampleMatcher MATCHER_ANY = ExampleMatcher.matchingAny()
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.exact().ignoreCase())
                .withMatcher("phoneNumber", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnorePaths("id", "password", "roles", "places", "blogs", "active", "activationCode");
        ExampleMatcher MATCHER_ALL = ExampleMatcher.matchingAll()
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.exact().ignoreCase())
                .withMatcher("phoneNumber", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnorePaths("id", "password", "roles", "places", "blogs", "active", "activationCode");
        return MATCH_ALL ? MATCHER_ALL:MATCHER_ANY;
    }

    private ExampleMatcher getExampleForAttribute(String attribute){
        if(attribute.equals("email")){
            return ExampleMatcher.matchingAny()
                    .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withIgnorePaths("id", "password", "roles", "places", "blogs", "firstName", "lastName",
                            "active", "activationCode", "phoneNumber");
        }
        return ExampleMatcher.matchingAny()
                .withMatcher("phoneNumber", ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnorePaths("id", "password", "roles", "places", "blogs", "firstName", "lastName",
                        "active", "activationCode", "email");
    }

}
