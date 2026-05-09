package com.tp.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tp.todolist.entity.ConfigEntity;

@Repository
public interface ConfigRepository extends JpaRepository<ConfigEntity, String> {

}