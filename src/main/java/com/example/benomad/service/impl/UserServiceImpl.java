package com.example.benomad.service.impl;

import com.example.benomad.entity.User;
import com.example.benomad.security.domain.Role;
import com.example.benomad.security.domain.UserDetailsImpl;
import com.example.benomad.security.jwt.JwtUtils;
import com.example.benomad.dto.UserDTO;
import com.example.benomad.mapper.UserMapper;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {


    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
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


    public UserDTO getNotDeletedUserById(Long id) {

        User user = userRepository.findById(id).get();

        return UserMapper.userToUserDto(user);
    }



    public boolean addUser(User user) {
        User userFromDb = userRepository.findByEmail(user.getEmail());

        if (userFromDb != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        userRepository.save(user);

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to BeNomad. Please, visit next link to activate your account: http://localhost:8080/api/auth/activate/%s",
                    user.getEmail(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Account activation", message);
        }

        return true;
    }




    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);

        userRepository.save(user);

        return true;
    }

}
