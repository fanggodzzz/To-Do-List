package com.tp.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tp.todolist.entity.TagsEntity;

@Repository
public interface TagsRepository extends JpaRepository<TagsEntity, Long> {

}