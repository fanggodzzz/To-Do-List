package com.tp.todolist.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tp.todolist.entity.ActivitiesEntity;
import com.tp.todolist.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;

    public List<ActivitiesEntity> getAll() {
        return todoRepository.findAll();
    }

    public ActivitiesEntity create(LocalDate dueDate, String description, Boolean completed) {
        ActivitiesEntity todo = ActivitiesEntity.builder()
                .ac_due_date(dueDate)
                .ac_description(description)
                .ac_completed(completed != null ? completed : false)
                .build();
        return todoRepository.save(todo);
    }

    public void updateTask(Long id, String description, LocalDate dueDate, Boolean completed) {
        ActivitiesEntity todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        if (description == null || description.trim().isEmpty()) {
            throw new RuntimeException("Description is required");
        }

        if (dueDate == null) {
            throw new RuntimeException("Due date is required");
        }

        if (completed == null) {
            throw new RuntimeException("Completed status is required");
        }

        todo.setAc_description(description.trim());
        todo.setAc_due_date(dueDate);
        todo.setAc_completed(completed);
        todoRepository.save(todo);
    }

    public void delete(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new RuntimeException("Todo not found");
        }
        todoRepository.deleteById(id);
    }

}
