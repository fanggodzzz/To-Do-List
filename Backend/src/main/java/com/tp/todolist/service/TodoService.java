package com.tp.todolist.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tp.todolist.entity.ActivitiesEntity;
import com.tp.todolist.entity.TagEntity;
import com.tp.todolist.repository.TagRepository;
import com.tp.todolist.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final TagRepository tagRepository;

    public List<ActivitiesEntity> getAll() {
        return todoRepository.findAll();
    }

    public List<ActivitiesEntity> getByMonth(Long year, Long month) {
        LocalDate start = LocalDate.of(year.intValue(), month.intValue(), 1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        return todoRepository.findByDueDateBetween(start, end);
    }

    // Todo operations
    public ActivitiesEntity createTask(LocalDate dueDate, String description, Boolean completed, Long tagId) {
        TagEntity tag = resolveTag(tagId);

        ActivitiesEntity todo = ActivitiesEntity.builder()
                .ac_due_date(dueDate)
                .ac_description(description)
                .ac_completed(completed != null ? completed : false)
                .ac_tag_id(tag)
                .build();
        return todoRepository.save(todo);
    }

    public void updateTask(Long id, String description, LocalDate dueDate, Boolean completed, Long tagId) {
        ActivitiesEntity todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        TagEntity tag = resolveTag(tagId);

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
        todo.setAc_tag_id(tag);
        todoRepository.save(todo);
    }

    public void deleteTask(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new RuntimeException("Todo not found");
        }
        todoRepository.deleteById(id);
    }

    // Tag endpoints
    public List<TagEntity> getAllTags() {
        return tagRepository.findAll();
    }

    public TagEntity getTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
    }

    public TagEntity addTag(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Tag name is required");
        }

        TagEntity tag = TagEntity.builder()
                .tag_name(name.trim())
                .build();

        return tagRepository.save(tag);
    }

    public TagEntity updateTag(Long id, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Tag name is required");
        }

        TagEntity tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
        tag.setTag_name(name.trim());
        return tagRepository.save(tag);
    }

    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new RuntimeException("Tag not found");
        }
        tagRepository.deleteById(id);
    }

    private TagEntity resolveTag(Long tagId) {
        if (tagId == null) {
            throw new RuntimeException("Tag is required");
        }

        return tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
    }

    // Upcoming activities
    public List<ActivitiesEntity> getUpcomingActivities() {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today
                .plusWeeks(1)
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        return todoRepository.findUpcoming(today, nextWeek);
    }

    // Overdue activities
    public List<ActivitiesEntity> getOverdueActivities() {
        LocalDate today = LocalDate.now();
        return todoRepository.findOverdue(today);
    }
}
