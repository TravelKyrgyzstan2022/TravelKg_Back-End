package com.example.benomad.service.impl;

import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.Place;
import com.example.benomad.entity.User;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.exception.ContentIsAlreadyInFavoritesException;
import com.example.benomad.exception.UserAttributeTakenException;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.mapper.UserMapper;
import com.example.benomad.repository.PlaceRepository;
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
    private final PlaceRepository placeRepository;

    @Override
    public UserDTO getUserById(Long userId) throws ContentNotFoundException {
        return userMapper.entityToDto(userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.USER, userId);
                }
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
        userRepository.save(userMapper.dtoToEntity(userDTO));
        userDTO.setId(userRepository.getLastValueOfSequence());
        return userDTO;
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

    public UserDTO updateUserById(Long userId, UserDTO userDTO) throws ContentNotFoundException {
        if(!userRepository.existsById(userId)){
            throw new ContentNotFoundException(ContentNotFoundEnum.USER, userId);
        }
        userDTO.setId(userId);
        userRepository.save(userMapper.dtoToEntity(userDTO));
        return userDTO;
    }

    @Override
    public UserDTO deleteUserById(Long userId) throws ContentNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.USER, userId);
                });
        if(userId != 1L){
            userRepository.delete(user);
        }
        return userMapper.entityToDto(user);
    }

    @Override
    public UserDTO addPlaceToFavorites(Long id, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> {
                   throw new ContentNotFoundException(ContentNotFoundEnum.USER,userId);
                });
        Place place = placeRepository.findById(id).orElseThrow(
                () -> {
                   throw new ContentNotFoundException(ContentNotFoundEnum.PLACE,id);
                });
        if (user.getPlaces().contains(place)) throw new ContentIsAlreadyInFavoritesException(ContentNotFoundEnum.PLACE);
        user.getPlaces().add(place);
        userRepository.save(user);
        //second idea is just deleting a place from user's favorites if place already exists , or add
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
