package com.tp.todolist.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.tp.todolist.dto.UserJWT;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret:GENERATE}")
    private String secret;

    @Value("${app.jwt.expiration-ms:3600000}")
    private long expirationTime;

    @PostConstruct
    private void initSecret() {

        if ("GENERATE".equals(secret)) {

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
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}