package com.tp.todolist.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "activities")
public class ActivitiesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ac_id;

    @Column(nullable = false)
    private LocalDate ac_due_date;

    @Column(nullable = false)
    private String ac_description;

    @Column(nullable = false)
    private Boolean ac_completed;

    @ManyToOne
    @JoinColumn(name = "ac_tag_id", referencedColumnName = "tag_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private TagEntity ac_tag_id;
}