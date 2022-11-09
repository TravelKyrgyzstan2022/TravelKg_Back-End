package com.example.benomad.repository;

import com.example.benomad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);

    Boolean existsByPhoneNumber(String phoneNumber);

    User findByEmail(String email);

    User findByActivationCode(String code);
}
