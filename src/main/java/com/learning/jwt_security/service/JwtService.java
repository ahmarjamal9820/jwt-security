package com.learning.jwt_security.service;

import com.learning.jwt_security.dto.TokenPair;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class JwtService {

    @Value("${app.jwt.secret}")
    private String jwtSecret;
    @Value("${app.jwt.expiry}")
    private Long accessTokenExpiry;
    @Value("${app.jwt.refresh-expiry}")
    private Long refreshTokenExpiry;

    public TokenPair generateTokenPair(Authentication authentication) {
        String accessToken = generateAccessToken(authentication);
        String refreshToken = generateRefreshToken(authentication);
        return new TokenPair(accessToken, refreshToken);
    }

    // Generate Access Token
    public String generateAccessToken(Authentication authentication){
        return generateToken(authentication, accessTokenExpiry, new HashMap<>());
    }

    // Generate Refresh Token
    public String generateRefreshToken(Authentication authentication){
        Map<String, String> claims = new HashMap<>();
        claims.put("tokenType", "refresh");

       return generateToken(authentication, refreshTokenExpiry, claims);
    }

    private String generateToken(Authentication authentication, Long tokenExpiry, Map<String, String> claims) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Date now = new Date(); // Time of Token creation
        Date expiryDate = new Date(now.getTime() + tokenExpiry);

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(userPrincipal.getUsername())
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignInKey())
                .compact();
    }

    // Validate Token
    public boolean validateTokenForUser(String token, UserDetails userDetails){
        final String username = extractUsernameFromToken(token);
        return username != null
                && username.equals(userDetails.getUsername());
    }

    public boolean isValidToken(String token){
        return extractAllClaims(token) != null;
    }

    public String extractUsernameFromToken(String token){
        Claims claims = extractAllClaims(token);
        if (claims != null){
            return claims.getSubject();
        }
        return null;
    }

    // Validate if the token is Refresh Token
    public boolean isRefreshToken(String token){
        Claims claims = extractAllClaims(token);
        if (claims == null){
            return false;
        }
        return "refresh".equals(claims.get("tokenType"));
    }

    private Claims extractAllClaims(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        return claims;
    }

    private SecretKey getSignInKey() {
        byte [] keyBytes = Decoders.BASE64URL.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
