package com.example.benomad.service.impl;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.User;
import com.example.benomad.exception.UserAttributeTakenException;
import com.example.benomad.exception.UserNotFoundException;
import com.example.benomad.mapper.UserMapper;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDTO getUserById(Long id) throws UserNotFoundException {
        return userMapper.entityToDto(userRepository.findById(id).orElseThrow(
                UserNotFoundException::new
        ));
    }

    @Override
    public UserDTO insertUser(UserDTO userDTO) throws UserAttributeTakenException {
        userDTO.setId(null);
        User user = User.builder()
                .login(userDTO.getLogin())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .build();

        Example<User> example = Example.of(user, getExampleForAttribute("login"));
        if(userRepository.findAll(example).size() > 0){
            throw new UserAttributeTakenException("login: ('" + userDTO.getLogin() + "')");
        }
        example = Example.of(user, getExampleForAttribute("email"));
        if(userRepository.findAll(example).size() > 0){
            throw new UserAttributeTakenException("email: ('" + userDTO.getEmail() + "')");
        }
        example = Example.of(user, getExampleForAttribute("phoneNumber"));
        if(userRepository.findAll(example).size() > 0){
            throw new UserAttributeTakenException("phone_number: ('" + userDTO.getPhoneNumber() + "')");
        }
        return userMapper.entityToDto(userRepository.save(userMapper.dtoToEntity(userDTO)));
    }

    @Override
    public List<UserDTO> getUsersByAttributes(String login, String firstName, String lastName,
                                              String email, String phoneNumber, boolean MATCH_ALL) {
        User user = User.builder()
                .login(login)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();

        Example<User> example = Example.of(user, getExample(MATCH_ALL));

        return userMapper.entityListToDtoList(userRepository.findAll(example));
    }

    @Override
    public UserDTO updateUserById(Long id, UserDTO userDTO) throws UserNotFoundException {
        userRepository.findById(id).orElseThrow(
                UserNotFoundException::new);
        userDTO.setId(id);
        userRepository.save(userMapper.dtoToEntity(userDTO));
        return userDTO;
    }

    @Override
    public UserDTO deleteUserById(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(
                UserNotFoundException::new);
        if(id != 1L){
            userRepository.delete(user);
        }
        return userMapper.entityToDto(user);
    }

    private ExampleMatcher getExample(boolean MATCH_ALL){
        ExampleMatcher MATCHER_ANY = ExampleMatcher.matchingAny()
                .withMatcher("login", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("phoneNumber", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnorePaths("id", "password", "roles", "places", "blogs");
        ExampleMatcher MATCHER_ALL = ExampleMatcher.matchingAll()
                .withMatcher("login", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("phoneNumber", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnorePaths("id", "password", "roles", "places", "blogs");
        return MATCH_ALL ? MATCHER_ALL:MATCHER_ANY;
    }

    private ExampleMatcher getExampleForAttribute(String attribute){
        if(attribute.equals("login")){
            return ExampleMatcher.matchingAny()
                    .withMatcher("login", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withIgnorePaths("id", "password", "roles", "places", "blogs", "firstName", "lastName",
                            "email", "phoneNumber");
        }
        if(attribute.equals("email")){
            return ExampleMatcher.matchingAny()
                    .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withIgnorePaths("id", "password", "roles", "places", "blogs", "firstName", "lastName",
                            "login", "phoneNumber");
        }
        return ExampleMatcher.matchingAny()
                .withMatcher("phoneNumber", ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnorePaths("id", "password", "roles", "places", "blogs", "firstName", "lastName",
                        "login", "email");
    }

}
