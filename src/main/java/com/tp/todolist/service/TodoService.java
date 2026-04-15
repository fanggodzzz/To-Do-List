package com.tp.todolist.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tp.todolist.entity.TodoEntity;
import com.tp.todolist.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;

    public List<TodoEntity> getAll() {
        return todoRepository.findAll();
    }

    public TodoEntity create(String title, String description, boolean completed) {
        TodoEntity todo = TodoEntity.builder()
                .title(title)
                .description(description)
                .completed(completed)
                .build();
        return todoRepository.save(todo);
    }

    public void update(Long id, boolean completed) {
        TodoEntity todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        todo.setCompleted(completed);
        todoRepository.save(todo);
    }

    public void delete(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new RuntimeException("Todo not found");
        }
        todoRepository.deleteById(id);
    }

}
