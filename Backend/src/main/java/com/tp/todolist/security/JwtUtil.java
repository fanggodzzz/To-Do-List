package com.tp.todolist.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import com.tp.todolist.dto.UserJWT;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Token validity: 1 hour
    private final long EXPIRATION_TIME = 1000 * 60 * 60;

    // Generate token
    public String generateToken(Long userId, String username, String email) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // store userId
                .claim("username", username)
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // Extract Authentication details from token
    public UserJWT extractUser(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return new UserJWT(claims.get("id", Long.class), claims.get("username", String.class),
                claims.get("email", String.class));
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}