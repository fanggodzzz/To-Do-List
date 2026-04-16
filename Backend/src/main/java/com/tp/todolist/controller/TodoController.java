package com.tp.todolist.controller;

import java.util.List;

import javax.swing.text.html.HTML.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tp.todolist.entity.ActivitiesEntity;
import com.tp.todolist.entity.TagEntity;
import com.tp.todolist.service.TodoService;

@RestController
@RequestMapping("/api/todo")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/")
    public List<ActivitiesEntity> getAllTodos() {
        return todoService.getAll();
    }

    @GetMapping("/tags")
    public List<TagEntity> getAllTags() {
        return todoService.getAllTags();
    }

    @GetMapping("/tags/{id}")
    public ResponseEntity<TagEntity> getTagById(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.getTagById(id));
    }

    @PostMapping("/tags/add")
    public ResponseEntity<TagEntity> addTag(@RequestBody TagEntity tag) {
        TagEntity createdTag = todoService.addTag(tag.getTag_name());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTag);
    }

    @DeleteMapping("/tags/delete/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        todoService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/tags/update/{id}")
    public ResponseEntity<TagEntity> updateTag(@PathVariable Long id, @RequestBody TagEntity tag) {
        return ResponseEntity.ok(todoService.updateTag(id, tag.getTag_name()));
    }

    @PostMapping("/add")
    public ResponseEntity<ActivitiesEntity> addTodo(@RequestBody ActivitiesEntity todo) {
        ActivitiesEntity createdTodo = todoService.createTask(
                todo.getAc_due_date(),
                todo.getAc_description(),
                todo.getAc_completed(),
                todo.getAc_tag_id() != null ? todo.getAc_tag_id().getTag_id() : null);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Void> updateTodo(@PathVariable Long id, @RequestBody ActivitiesEntity todo) {
        todoService.updateTask(
                id,
                todo.getAc_description(),
                todo.getAc_due_date(),
                todo.getAc_completed(),
                todo.getAc_tag_id() != null ? todo.getAc_tag_id().getTag_id() : null);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
