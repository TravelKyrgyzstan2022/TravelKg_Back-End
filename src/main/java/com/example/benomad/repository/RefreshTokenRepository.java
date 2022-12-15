package com.example.benomad.repository;

import com.example.benomad.entity.RefreshToken;
import com.example.benomad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
    boolean existsByUser(User user);

    @Modifying
    int deleteByUser(User user);
}
