package com.student.auth;

import com.student.exception.EmailAlreadyExistsException;
import com.student.exception.InvalidCredentialsException;
import com.student.exception.InvalidRefreshTokenException;
import com.student.refreshtoken.RefreshToken;
import com.student.refreshtoken.RefreshTokenRequest;
import com.student.refreshtoken.RefreshTokenService;
import com.student.user.Role;
import com.student.user.User;
import com.student.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserRepository userRepository, JwtService jwtService,
                                 PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
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

}
