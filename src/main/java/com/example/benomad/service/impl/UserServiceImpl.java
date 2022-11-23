package com.example.benomad.service.impl;

import com.example.benomad.dto.DeletionInfoDTO;
import com.example.benomad.entity.User;

import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.exception.UserAttributeTakenException;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.logger.LogWriterServiceImpl;
import com.example.benomad.mapper.DeletionInfoMapper;
import com.example.benomad.security.domain.Role;
import com.example.benomad.security.domain.UserDetailsImpl;
import com.example.benomad.util.JwtUtils;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
    private final LogWriterServiceImpl logWriter;
    private final PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> {throw new ContentNotFoundException(ContentNotFoundEnum.USER, "email", email);}
        );
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


    public UserDTO addUser(UserDTO userDTO) {

        User user = userMapper.dtoToEntity(userDTO);

        if(userRepository.existsByEmail(userDTO.getEmail())){
            throw new UserAttributeTakenException("email: ('" + user.getEmail() + "')");
        }

        user.setId(null);
        user.setActivated(false);
        user.setRegistrationDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setPassword(encoder.encode(user.getPassword()));

        userRepository.save(user);

        logWriter.auth(String.format("%s - Registration completed", userDTO.getEmail()));
        return userMapper.entityToDto(user);
    }

    @Override
    public UserDTO getUserById(Long userId) throws ContentNotFoundException {
        UserDTO userDTO = userMapper.entityToDto(getUserEntityById(userId));
        logWriter.get(String.format("%s - Returned user with id = %d", getAuthName(), userId));
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
        logWriter.insert(String.format("%s - Inserted user with id = %d", getAuthName(), userDTO.getId()));
        return userDTO;
    }

    //fixme
    public void setDeleted(){}
    public void sameGoesForBlogsBro(){}

    public void setActivated(String email){
        User user = getUserEntityByEmail(email);
        user.setActivated(true);
        userRepository.save(user);
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
        List<UserDTO> userDTOS = userMapper.entityListToDtoList(userRepository.findAll(example, Sort.by(Sort.Direction.ASC, "id")));
        logWriter.get(String.format("%s - Returned %d users", getAuthName(), userDTOS.size()));
        return userDTOS;
    }

    @Override
    public UserDTO getUserByEmail(String email){
        return userMapper.entityToDto(getUserEntityByEmail(email));
    }

    @Override
    public UserDTO getCurrentUser() {
        return userMapper.entityToDto(getUserEntityByEmail(getAuthName()));
    }

    @Override
    public UserDTO updateCurrentUser(UserDTO userDTO) {
        userDTO.setEmail(getAuthName());
        userDTO.setId(getUserEntityByEmail(getAuthName()).getId());
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
        if(!isAdmin || getAuthName().equals(userDTO.getEmail())){
            userDTO.setId(userId);
            userRepository.save(userMapper.dtoToEntity(userDTO));
        }
        logWriter.update(String.format("%s - Updated user with id - %d", getAuthName(), userId));
        return userDTO;
    }

    @Override
    //fixme
    public UserDTO deleteUserById(Long userId, DeletionInfoDTO infoDTO) throws ContentNotFoundException {
        User user = getUserEntityById(userId);
        boolean isAdmin = !user.getRoles().contains(Role.ROLE_ADMIN);
        if(!isAdmin){
            user.setDeleted(true);
            infoDTO.setDeletionDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
            user.setDeletionInfo(deletionInfoMapper.dtoToEntity(infoDTO));
            userRepository.save(user);
        }
        logWriter.delete(String.format("%s - %s with id - %d", getAuthName(),
                isAdmin ? "Couldn't delete admin" : "Deleted user", userId));
        return userMapper.entityToDto(user);
    }

    public User getUserEntityByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(
                () -> new ContentNotFoundException(ContentNotFoundEnum.USER, "email", email)
        );
    }

    public void resetPassword(String email, String password){
        User user = getUserEntityByEmail(email);
        user.setPassword(encoder.encode(password));
        userRepository.save(user);
    }

    public User getUserEntityById(Long userId){
        return userRepository.findById(userId).orElseThrow(
                () -> new ContentNotFoundException(ContentNotFoundEnum.USER, "id", String.valueOf(userId))
        );
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
