package com.tp.todolist.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tp.todolist.entity.UsersEntity;
import com.tp.todolist.service.UserService;
import com.tp.todolist.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public void register(UsersEntity request) {
        String username = request.getUser_name();
        String password = request.getUser_password();
        String email = request.getUser_email();

        try {
            userService.getUserByUsername(username);
            throw new RuntimeException("Username already exists");
        } catch (RuntimeException e) {
            // User not found, continue
        }

        try {
            userService.getUserByEmail(email);
            throw new RuntimeException("Email already exists");
        } catch (RuntimeException e) {
            // User not found, continue
        }

        String hashedPassword = passwordEncoder.encode(password);

        UsersEntity newUser = UsersEntity.builder()
                .user_name(username)
                .user_password(hashedPassword)
                .user_email(email)
                .build();

        userService.createUser(newUser);
    }

    public String login(UsersEntity request) {
        String username = request.getUser_name();
        String password = request.getUser_password();

        UsersEntity user = userService.getUserByUsername(username);

        if (!passwordEncoder.matches(password, user.getUser_password())) {
            throw new RuntimeException("Invalid username or password");
        }

        return jwtUtil.generateToken(user.getUser_id());
    }

}
