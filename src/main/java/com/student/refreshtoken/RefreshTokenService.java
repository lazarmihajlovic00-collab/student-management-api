package com.student.refreshtoken;

import com.student.exception.RefreshTokenNotFoundException;
import com.student.user.User;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpirationDate(LocalDateTime.now().plusSeconds(refreshExpiration/1000));
        refreshToken.setRevoked(false);
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(()->new RefreshTokenNotFoundException("RefreshToken not found"));
    }

    public boolean isValid(RefreshToken refreshToken) {
        return !refreshToken.isRevoked() && refreshToken.getExpirationDate()
                .isAfter(LocalDateTime.now());
    }

    public void revokeRefreshToken(String token) {
        RefreshToken refreshToken = findByToken(token);
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

}
