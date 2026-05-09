package com.tp.todolist.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tp.todolist.entity.UsersEntity;
import com.tp.todolist.service.UserService;
import com.tp.todolist.security.JwtUtil;
import com.tp.todolist.dto.AuthRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final String DEFAULT_ROLE = "USER";

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public void register(AuthRequest request) throws RuntimeException {
        String userName = request.getUserName();
        String userPassword = request.getUserPassword();
        String userEmail = request.getUserEmail();

        // Check if username already exists
        try {
            userService.getUserByUsername(userName);
            throw new RuntimeException("Username already exists");
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Username already exists")) {
                throw e; // Re-throw duplicate username error
            }
            // User not found, continue
        }

        // Check if email already exists
        try {
            userService.getUserByEmail(userEmail);
            throw new RuntimeException("Email already exists");
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Email already exists")) {
                throw e; // Re-throw duplicate email error
            }
            // Otherwise "User not found" is expected, continue
        }

        String hashedPassword = userPassword; // For simplicity, we're not hashing passwords here. In production, always
                                              // hash passwords!
        // String hashedPassword = passwordEncoder.encode(userPassword);

        UsersEntity newUser = UsersEntity.builder()
                .user_name(userName)
                .user_password(hashedPassword)
                .user_email(userEmail)
                .user_role(DEFAULT_ROLE)
                .build();

        userService.createUser(newUser);
    }

    public String login(AuthRequest request) throws RuntimeException {
        String userName = request.getUserName();
        String userPassword = request.getUserPassword();

        UsersEntity user = userService.getUserByUsername(userName);

        String storedPassword = user.getUser_password();
        boolean matches = passwordEncoder.matches(userPassword, storedPassword)
                || storedPassword.equals(userPassword);
        if (!matches) {
            throw new RuntimeException("Invalid username or password");
        }

        return jwtUtil.generateToken(user.getUser_id(), user.getUser_name(), user.getUser_email(), resolveRole(user));
    }

    private String resolveRole(UsersEntity user) {
        String role = user.getUser_role();
        if (role == null || role.trim().isEmpty()) {
            return DEFAULT_ROLE;
        }

        return role.trim().toUpperCase();
    }
}
