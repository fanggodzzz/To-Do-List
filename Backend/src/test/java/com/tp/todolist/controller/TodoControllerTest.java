package com.tp.todolist.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.tp.todolist.dto.TagsDTO;
import com.tp.todolist.security.JwtUtil;
import com.tp.todolist.service.ActivitiesService;

@WebMvcTest(TodoController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for testing
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ActivitiesService activitiesService;

    @MockitoBean
    private JwtUtil jwtUtil;

    private TagsDTO testTagDTO;

    @BeforeEach
    void setUp() {
        testTagDTO = new TagsDTO(1L, "Work");
    }

    @Test
    void testGetAllTags_Success() throws Exception {
        when(activitiesService.getAllTags()).thenReturn(List.of(testTagDTO));

        mockMvc.perform(get("/api/todos/tags")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tagName").value("Work"));
    }
}
