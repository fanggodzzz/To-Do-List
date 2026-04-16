package com.tp.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tp.todolist.entity.TagEntity;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

}