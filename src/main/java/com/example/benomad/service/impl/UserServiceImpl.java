package com.example.benomad.service.impl;

import com.example.benomad.dto.MessageResponse;
import com.example.benomad.entity.User;

import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.exception.UserAttributeTakenException;
import com.example.benomad.exception.ContentNotFoundException;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {


    private final UserRepository userRepository;
    private final UserMapper userMapper;
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

        Example<User> example = Example.of(user, getExampleForAttribute("email"));
        if(userRepository.findAll(example).size() > 0){
            throw new UserAttributeTakenException("email: ('" + user.getEmail() + "')");
        }
//        example = Example.of(user, getExampleForAttribute("phoneNumber"));
//        if(userRepository.findAll(example).size() > 0){
//            throw new UserAttributeTakenException("phone_number: ('" + user.getPhoneNumber() + "')");
//        }

        user.setId(null);
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

//        if (!StringUtils.isEmpty(user.getEmail())) {
//            String message = String.format(
//                    "Hello, %s! \n" +
//                            "Welcome to BeNomad. Please, visit next link to activate your account: http://localhost:8080/api/auth/activate/%s",
//                    user.getEmail(),
//                    user.getActivationCode()
//            );
//
//            mailSender.send(user.getEmail(), "Account activation", message);
//        }
        log.info("Registered new user with email - " + userDTO.getEmail());
        return userMapper.entityToDto(user);
    }

//    public boolean activateUser(String code) {
//        User user = userRepository.findByActivationCode(code);
//
//        if (user == null) {
//            return false;
//        }
//
//        user.setActivationCode(null);
//
//        userRepository.save(user);
//
//        System.out.println(getUserAuthenticationToken(user));
//
//        return true;
//    }
    
    @Override
    public UserDTO getUserById(Long userId) throws ContentNotFoundException {
        return userMapper.entityToDto(userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.USER, "id", String.valueOf(userId));
                }
        ));
    }

    @Override
    public UserDTO insertUser(UserDTO userDTO) throws UserAttributeTakenException {
        userDTO.setId(null);
        User user = User.builder()
                .email(userDTO.getEmail())
                .build();
        if(!user.getEmail().equals(userDTO.getEmail())){
            Example<User> example = Example.of(user, getExampleForAttribute("email"));
            if(userRepository.findAll(example).size() > 0){
                throw new UserAttributeTakenException("email: ('" + user.getEmail() + "')");
            }
        }

        userRepository.save(userMapper.dtoToEntity(userDTO));
        userDTO.setId(userRepository.getLastValueOfSequence());
        return userDTO;
    }

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

        log.info("Returned all users to admin with email - "
                + SecurityContextHolder.getContext().getAuthentication().getName());

        return userMapper.entityListToDtoList(userRepository.findAll(example));
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
        if(!user.getEmail().equals(userDTO.getEmail())){
            Example<User> example = Example.of(user, getExampleForAttribute("email"));
            if(userRepository.findAll(example).size() > 0){
                throw new UserAttributeTakenException("email: ('" + user.getEmail() + "')");
            }
        }
        if(!Objects.equals(user.getPhoneNumber(), userDTO.getPhoneNumber())){
            Example<User> example = Example.of(user, getExampleForAttribute("phoneNumber"));
            if(userRepository.findAll(example).size() > 0){
                throw new UserAttributeTakenException("phone_number: ('" + user.getPhoneNumber() + "')");
            }
        }
        userDTO.setId(userId);
        userDTO.setEmail(user.getEmail());
        userRepository.save(userMapper.dtoToEntity(userDTO));
        log.info("Updated user with id - " + userId);
        return userDTO;
    }

    @Override
    public UserDTO deleteUserById(Long userId) throws ContentNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.USER, "id", String.valueOf(userId));
                });
        if(userId != 1L){
            userRepository.delete(user);
        }
        log.info("Deleted user with id - " + userId);
        return userMapper.entityToDto(user);
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
