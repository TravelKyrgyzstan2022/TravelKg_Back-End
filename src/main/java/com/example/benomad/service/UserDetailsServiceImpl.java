package com.example.benomad.service;


import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.Status;
import com.example.benomad.entity.User;
import com.example.benomad.mapper.UserMapper;
import com.example.benomad.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;


    @Autowired
    public UserDetailsServiceImpl(@Lazy PasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }



    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login).orElseThrow(() ->
                new UsernameNotFoundException("User doesn't exists"));
        return SecurityUser.fromUser(user);
    }

    public User save(UserDTO userDTO){
        User newUser = UserMapper.dtoToEntity(userDTO);
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
//        newUser.setPassword("mmm");
        return userRepository.save(newUser);
    }
}
