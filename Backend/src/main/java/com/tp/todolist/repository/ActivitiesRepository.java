package com.tp.todolist.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tp.todolist.entity.ActivitiesEntity;

@Repository
public interface ActivitiesRepository extends JpaRepository<ActivitiesEntity, Long> {

    @Query("SELECT a FROM ActivitiesEntity a WHERE a.ac_due_date BETWEEN :start AND :end")
    List<ActivitiesEntity> findByDueDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT a FROM ActivitiesEntity a WHERE a.ac_due_date BETWEEN :start AND :end AND a.ac_completed = false")
    List<ActivitiesEntity> findUpcoming(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT a FROM ActivitiesEntity a WHERE a.ac_due_date < :today AND a.ac_completed = false")
    List<ActivitiesEntity> findOverdue(@Param("today") LocalDate today);

}