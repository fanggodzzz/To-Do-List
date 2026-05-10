package com.tp.todolist.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tp.todolist.dto.ActivitiesDTO;
import com.tp.todolist.dto.TagsDTO;
import com.tp.todolist.entity.ActivitiesEntity;
import com.tp.todolist.entity.TagsEntity;
import com.tp.todolist.entity.UsersEntity;
import com.tp.todolist.repository.ActivitiesRepository;
import com.tp.todolist.repository.TagsRepository;

/**
 * Unit tests for ActivitiesService using JUnit 5 and Mockito.
 * Tests CRUD operations for tasks and tags with mocked repositories.
 */
@ExtendWith(MockitoExtension.class)
class ActivitiesServiceTest {

    @Mock
    private ActivitiesRepository activitiesRepository;

    @Mock
    private TagsRepository tagRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ActivitiesService activitiesService;

    private ActivitiesEntity testActivity;
    private TagsEntity testTag;
    private UsersEntity testUser;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.now();

        testUser = UsersEntity.builder()
                .user_id(1L)
                .user_name("testuser")
                .user_email("test@example.com")
                .user_password("hashed_password")
                .build();

        testTag = TagsEntity.builder()
                .tag_id(1L)
                .tag_name("Work")
                .build();

        testActivity = ActivitiesEntity.builder()
                .ac_id(1L)
                .ac_due_date(testDate)
                .ac_description("Test task")
                .ac_completed(false)
                .ac_tag_id(testTag)
                .ac_user_id(testUser)
                .build();
    }

    @Test
    void testCreateTask_Success() {
        when(tagRepository.findById(anyLong())).thenReturn(Optional.of(testTag));
        when(userService.getUserById(anyLong())).thenReturn(testUser);
        when(activitiesRepository.save(any(ActivitiesEntity.class))).thenReturn(testActivity);

        ActivitiesDTO result = activitiesService.createTask(
                1L,
                testDate,
                "Test task",
                false,
                1L);

        assertNotNull(result);
        assertEquals("Test task", result.getAcDescription());
        assertEquals(testDate, result.getAcDueDate());
        assertEquals(1L, result.getAcTagId());
        assertFalse(result.getAcCompleted());
        verify(activitiesRepository, times(1)).save(any(ActivitiesEntity.class));
    }

    @Test
    void testGetByMonth_Success() {
        when(activitiesRepository.findByDueDateBetweenAndUserId(any(), any(), anyLong()))
                .thenReturn(java.util.List.of(testActivity));

        java.util.List<ActivitiesDTO> result = activitiesService.getByMonth(2026L, 5L, 1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test task", result.get(0).getAcDescription());
    }
}
