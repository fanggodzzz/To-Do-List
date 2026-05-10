package com.tp.todolist.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp.todolist.dto.AuthRequest;
import com.tp.todolist.security.JwtUtil;
import com.tp.todolist.service.AuthService;

/**
 * Tests for AuthController using Spring Boot Test and MockMvc.
 * Covers registration and login endpoints with various scenarios.
 */
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private AuthRequest validRequest;
    private AuthRequest invalidRequest;

    @BeforeEach
    void setUp() {
        validRequest = new AuthRequest();
        validRequest.setUserName("testuser");
        validRequest.setUserPassword("SecurePass123!");

        invalidRequest = new AuthRequest();
        invalidRequest.setUserName("");
        invalidRequest.setUserPassword("");
    }

    // --- Registration Tests ---

    @Test
    void testRegister_Success() throws Exception {
        doNothing().when(authService).register(any(AuthRequest.class));

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void testRegister_UserAlreadyExists() throws Exception {
        doThrow(new RuntimeException("Username already exists"))
                .when(authService).register(any(AuthRequest.class));

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already exists"));
    }

    // --- Login Tests ---

    @Test
    void testLogin_Success() throws Exception {
        String mockToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        when(authService.login(any(AuthRequest.class))).thenReturn(mockToken);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(mockToken));
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        doThrow(new RuntimeException("Invalid username or password"))
                .when(authService).login(any(AuthRequest.class));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }

    @Test
    void testLogin_UserNotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException("User not found"))
                .when(authService).login(any(AuthRequest.class));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isUnauthorized());
    }
}
