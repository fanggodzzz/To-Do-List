package com.tp.todolist.controller;

import java.util.List;

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

    @PostMapping("/add")
    public ResponseEntity<ActivitiesEntity> addTodo(@RequestBody ActivitiesEntity todo) {
        ActivitiesEntity createdTodo = todoService.create(
                todo.getAc_due_date(),
                todo.getAc_description(),
                todo.getAc_completed());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Void> updateDueDate(@PathVariable Long id, @RequestBody ActivitiesEntity todo) {
        todoService.updateTask(id, todo.getAc_description(), todo.getAc_due_date(), todo.getAc_completed());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
