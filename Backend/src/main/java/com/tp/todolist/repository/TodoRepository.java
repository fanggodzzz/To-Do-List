package com.tp.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tp.todolist.entity.ActivitiesEntity;

@Repository
public interface TodoRepository extends JpaRepository<ActivitiesEntity, Long> {

}