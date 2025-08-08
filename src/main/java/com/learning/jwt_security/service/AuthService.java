package com.learning.jwt_security.service;

import com.learning.jwt_security.dto.LoginRequest;
import com.learning.jwt_security.dto.RefreshTokenRequest;
import com.learning.jwt_security.dto.RegisterRequest;
import com.learning.jwt_security.dto.TokenPair;
import com.learning.jwt_security.model.User;
import com.learning.jwt_security.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void registerUser(RegisterRequest request){
        // Check if username is already taken
        if (userRepo.existsByUsername(request.getUsername())){
            throw new IllegalArgumentException("Username is already in use");
        }
        // Create new user
        User user = User
                .builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepo.save(user);
    }

    public TokenPair login(LoginRequest request){
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Set the authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate the token pair
        return jwtService.generateTokenPair(authentication);
    }

    public TokenPair refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        // Check if it is a valid refresh token
        if (!jwtService.isRefreshToken(refreshToken)){
            throw new IllegalArgumentException("Invalid Refresh Token");
        }

        String user = jwtService.extractUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user);

        if (userDetails == null){
            throw new IllegalArgumentException("User not found");
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        String accessToken = jwtService.generateAccessToken(authentication);
        return new TokenPair(accessToken, refreshToken);
    }
}
