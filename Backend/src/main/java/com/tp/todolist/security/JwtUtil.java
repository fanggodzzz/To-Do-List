package com.tp.todolist.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tp.todolist.dto.UserJWT;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${app.jwt.secret:GENERATE_A_RANDOM_SECRET}")
    private String secret;

    @Value("${app.jwt.expiration-ms:3600000}")
    private long expirationTime;

    @PostConstruct
    private void initSecret() {

        if ("GENERATE_A_RANDOM_SECRET".equals(secret)) {

            byte[] keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS256)
                    .getEncoded();

            secret = Base64.getEncoder().encodeToString(keyBytes);

            System.out.println("Generated JWT Secret:");
            System.out.println(secret);
        }
    }

    private Key getSigningKey() {
        // The secret is stored as Base64 when generated. When reading the
        // secret, try to decode Base64 first; if that fails, fall back to
        // the raw UTF-8 bytes (for backwards compatibility).
        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(secret);
        } catch (IllegalArgumentException ex) {
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate token
    public String generateToken(Long userId, String username, String email, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // store userId
                .claim("username", username)
                .claim("email", email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    // Extract Authentication details from token
    public UserJWT extractUser(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        Long id = Long.valueOf(claims.getSubject());
        return new UserJWT(id, claims.get("username", String.class), claims.get("email", String.class),
                claims.get("role", String.class));
    }

    // Validate token
    public String getTokenValidationError(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return null;
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token expired at {}", e.getClaims().getExpiration());
            return "JWT token has expired";
        } catch (UnsupportedJwtException e) {
            logger.warn("Unsupported JWT token: {}", e.getMessage());
            return "Unsupported JWT token";
        } catch (MalformedJwtException e) {
            logger.warn("Malformed JWT token: {}", e.getMessage());
            return "Malformed JWT token";
        } catch (SignatureException e) {
            logger.warn("Invalid JWT signature: {}", e.getMessage());
            return "Invalid JWT signature";
        } catch (IllegalArgumentException e) {
            logger.warn("JWT token is empty: {}", e.getMessage());
            return "JWT token is empty";
        } catch (Exception e) {
            logger.warn("JWT validation failed: {}", e.getMessage());
            return "JWT validation failed";
        }
    }

    public boolean validateToken(String token) {
        return getTokenValidationError(token) == null;
    }
}