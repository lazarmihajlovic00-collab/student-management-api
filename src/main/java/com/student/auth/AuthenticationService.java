package com.student.auth;

import com.student.exception.EmailAlreadyExistsException;
import com.student.exception.InvalidCredentialsException;
import com.student.exception.InvalidRefreshTokenException;
import com.student.refreshtoken.RefreshToken;
import com.student.refreshtoken.RefreshTokenRepository;
import com.student.refreshtoken.RefreshTokenRequest;
import com.student.refreshtoken.RefreshTokenService;
import com.student.user.Role;
import com.student.user.User;
import com.student.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthenticationService(UserRepository userRepository, JwtService jwtService,
                                 PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService,
                                 RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setRole(Role.USER);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        return new AuthenticationResponse(accessToken, refreshToken.getToken());
    }

    public AuthenticationResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new InvalidCredentialsException("Invalid credentials"));
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!passwordMatches){
            throw new InvalidCredentialsException("Invalid credentials");
        }

        String token =  jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        return new AuthenticationResponse(token,  refreshToken.getToken());
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken());

        if(!refreshTokenService.isValid(refreshToken)){
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        User user = refreshToken.getUser();
        String accessToken = jwtService.generateToken(user);
        return new AuthenticationResponse(accessToken, refreshToken.getToken());
    }

    public void logout(RefreshTokenRequest request) {
        refreshTokenService.revokeRefreshToken(request.getRefreshToken());
    }

    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            String email = jwtService.extractEmail(jwt);

            if (email != null) {
                userRepository.findByEmail(email).ifPresent(user -> {
                    refreshTokenRepository.deleteByUserId(user.getId());
                    log.info("Korisnik {} se uspešno odjavio. Refresh tokeni su obrisani iz baze.", email);
                });
            }
        }
    }
}