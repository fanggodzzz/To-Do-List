package com.tp.todolist.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.tp.todolist.service.ActivitiesService;
import com.tp.todolist.dto.TagsDTO;
import com.tp.todolist.dto.ActivitiesDTO;
import com.tp.todolist.dto.UserJWT;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final ActivitiesService todoService;

    // --- Todo endpoints ---

    @GetMapping("/")
    public List<ActivitiesDTO> getByMonth(
            @RequestParam Long year,
            @RequestParam Long month,
            @AuthenticationPrincipal UserJWT user) {
        return todoService.getByMonth(year, month, user.getUserId());
    }

    @PostMapping("/add")
    public ResponseEntity<ActivitiesDTO> addTodo(
            @RequestBody ActivitiesDTO todo,
            @AuthenticationPrincipal UserJWT user) {
        ActivitiesDTO createdTodo = todoService.createTask(
                user.getUserId(),
                todo.getAcDueDate(),
                todo.getAcDescription(),
                todo.getAcCompleted(),
                todo.getAcTagId());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }

    @PatchMapping("/update/")
    public ResponseEntity<Void> updateTodo(
            @RequestBody ActivitiesDTO todo,
            @AuthenticationPrincipal UserJWT user) {
        todoService.isOwner(user.getUserId(), todo.getAcId());

        todoService.updateTask(
                todo.getAcId(),
                todo.getAcDescription(),
                todo.getAcDueDate(),
                todo.getAcCompleted(),
                todo.getAcTagId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable Long id,
            @AuthenticationPrincipal UserJWT user) {
        todoService.isOwner(user.getUserId(), id);

        todoService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/upcoming")
    public List<ActivitiesDTO> getUpcomingActivities(
            @AuthenticationPrincipal UserJWT user) {
        return todoService.getUpcomingActivities(user.getUserId());
    }

    @GetMapping("/overdue")
    public List<ActivitiesDTO> getOverdueActivities(
            @AuthenticationPrincipal UserJWT user) {
        return todoService.getOverdueActivities(user.getUserId());
    }

    // --- Tag endpoints ---

    @GetMapping("/tags")
    public List<TagsDTO> getAllTags() {
        return todoService.getAllTags();
    }

    @GetMapping("/tags/{id}")
    public ResponseEntity<TagsDTO> getTagById(
            @PathVariable Long id) {
        return ResponseEntity.ok(todoService.getTagById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/tags/add")
    public ResponseEntity<TagsDTO> addTag(
            @RequestBody TagsDTO tag) {
        TagsDTO createdTag = todoService.addTag(tag.getTagName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTag);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/tags/delete/{id}")
    public ResponseEntity<Void> deleteTag(
            @PathVariable Long id) {
        todoService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/tags/update/")
    public ResponseEntity<TagsDTO> updateTag(
            @RequestBody TagsDTO tag) {
        return ResponseEntity.ok(todoService.updateTag(tag.getTagId(), tag.getTagName()));
    }

}
