package com.tp.todolist.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.tp.todolist.dto.ActivitiesDTO;
import com.tp.todolist.dto.TagsDTO;
import com.tp.todolist.entity.ActivitiesEntity;
import com.tp.todolist.entity.TagsEntity;
import com.tp.todolist.entity.UsersEntity;
import com.tp.todolist.repository.ActivitiesRepository;
import com.tp.todolist.repository.TagsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivitiesService {
    private final ActivitiesRepository todoRepository;
    private final TagsRepository tagRepository;
    private final UserService userService;

    // --- Transform the entity to DTO for response ---

    public ActivitiesDTO activityDtoTransform(ActivitiesEntity entity) {
        return new ActivitiesDTO(
                entity.getAc_id(),
                entity.getAc_due_date(),
                entity.getAc_description(),
                entity.getAc_tag_id().getTag_id(),
                entity.getAc_completed());
    }

    public List<ActivitiesDTO> activitiesListDtoTransform(List<ActivitiesEntity> entities) {
        List<ActivitiesDTO> dtos = new ArrayList<>();
        for (ActivitiesEntity entity : entities) {
            dtos.add(activityDtoTransform(entity));
        }
        return dtos;
    }

    public TagsDTO tagDTOTransform(TagsEntity tag) {
        if (tag == null) {
            return null;
        }
        return new TagsDTO(tag.getTag_id(), tag.getTag_name());
    }

    public List<TagsDTO> tagsListDTOTransform(List<TagsEntity> entities) {
        List<TagsDTO> dtos = new ArrayList<>();
        for (TagsEntity entity : entities) {
            dtos.add(tagDTOTransform(entity));
        }
        return dtos;
    }

    // --- Ownership checks ---

    public void isOwner(Long userId, Long acId) {
        ActivitiesEntity activity = todoRepository.findById(acId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
        if (!activity.getAc_user_id().getUser_id().equals(userId)) {
            throw new RuntimeException("Forbidden: You do not own this activity");
        }
    }

    // --- Todo CRUD operations ---

    public List<ActivitiesDTO> getByMonth(
            Long year,
            Long month,
            Long userId) {
        LocalDate start = LocalDate.of(year.intValue(), month.intValue(), 1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        return activitiesListDtoTransform(todoRepository.findByDueDateBetweenAndUserId(start, end, userId));
    }

    public ActivitiesDTO createTask(
            Long userId,
            LocalDate dueDate,
            String description,
            Boolean completed,
            Long tagId) {
        TagsEntity tag = resolveTag(tagId);
        UsersEntity user = userService.getUserById(userId);

        ActivitiesEntity todo = ActivitiesEntity.builder()
                .ac_due_date(dueDate)
                .ac_description(description)
                .ac_completed(completed != null ? completed : false)
                .ac_tag_id(tag)
                .ac_user_id(user)
                .build();
        return activityDtoTransform(todoRepository.save(todo));
    }

    public ActivitiesDTO updateTask(
            Long id,
            String description,
            LocalDate dueDate,
            Boolean completed,
            Long tagId) {
        ActivitiesEntity todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));

        TagsEntity tag = resolveTag(tagId);

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
        return activityDtoTransform(todoRepository.save(todo));
    }

    public void deleteTask(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new RuntimeException("Todo not found");
        }
        todoRepository.deleteById(id);
    }

    public List<ActivitiesDTO> getUpcomingActivities(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today
                .plusWeeks(1)
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        return activitiesListDtoTransform(todoRepository.findUpcomingAndUserId(today, nextWeek, userId));
    }

    public List<ActivitiesDTO> getOverdueActivities(Long userId) {
        LocalDate today = LocalDate.now();
        return activitiesListDtoTransform(todoRepository.findOverdueAndUserId(today, userId));
    }

    // --- Tag endpoints ---

    public List<TagsDTO> getAllTags() {
        return tagsListDTOTransform(tagRepository.findAll());
    }

    public TagsDTO getTagById(Long id) {
        return tagDTOTransform(tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found")));
    }

    // Admin-only tag management
    @PreAuthorize("hasRole('ADMIN')")
    public TagsDTO addTag(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Tag name is required");
        }

        TagsEntity tag = TagsEntity.builder()
                .tag_name(name.trim())
                .build();

        return tagDTOTransform(tagRepository.save(tag));
    }

    // Admin-only tag management
    @PreAuthorize("hasRole('ADMIN')")
    public TagsDTO updateTag(Long id, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Tag name is required");
        }

        TagsEntity tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
        tag.setTag_name(name.trim());
        return tagDTOTransform(tagRepository.save(tag));
    }

    // Admin-only tag management
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new RuntimeException("Tag not found");
        }
        tagRepository.deleteById(id);
    }

    private TagsEntity resolveTag(Long tagId) {
        if (tagId == null) {
            throw new RuntimeException("Tag is required");
        }

        return tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
    }
}
