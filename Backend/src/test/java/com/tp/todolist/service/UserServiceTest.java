package com.tp.todolist.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tp.todolist.entity.UsersEntity;
import com.tp.todolist.repository.UsersRepository;

/**
 * Unit tests for UserService using JUnit 5 and Mockito.
 * Tests user-related queries and CRUD operations.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private UserService userService;

    private UsersEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = UsersEntity.builder()
                .user_id(1L)
                .user_name("testuser")
                .user_email("test@example.com")
                .user_password("hashed_password")
                .user_role("USER")
                .build();
    }

    @Test
    void testGetUserById_Success() {
        when(usersRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UsersEntity result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("testuser", result.getUser_name());
        assertEquals("test@example.com", result.getUser_email());
        assertEquals("hashed_password", result.getUser_password());
        assertEquals("USER", result.getUser_role());
    }
}
