package com.example.benomad.service.impl;

import com.example.benomad.repository.UserRepository;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Data
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private static  final String USER_NOT_FOUND_MESSAGE = "User with username %s has not been found";
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE,username)));
    }
}
