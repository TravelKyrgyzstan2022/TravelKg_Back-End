package com.example.benomad.service.impl;

import com.example.benomad.entity.RefreshToken;
import com.example.benomad.entity.User;
import com.example.benomad.exception.RefreshTokenException;
import com.example.benomad.repository.RefreshTokenRepository;
import com.example.benomad.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserServiceImpl userService;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        User user = userService.getUserEntityById(userId);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        if (refreshTokenRepository.existsByUser(user)) {
            RefreshToken oldToken = refreshTokenRepository.findByUser(user).get();
            refreshToken.setId(oldToken.getId());
        }
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException(token.getToken(), "Refresh token was expired. Please make a new sign in request.");
        }
        return token;
    }

    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userService.getUserEntityById(userId));
    }
}
