package com.tp.todolist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tp.todolist.entity.UsersEntity;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, Long> {

    @Query("SELECT u FROM UsersEntity u WHERE u.user_name = :username")
    Optional<UsersEntity> findByUserName(@Param("username") String username);

    @Query("SELECT u FROM UsersEntity u WHERE u.user_email = :email")
    Optional<UsersEntity> findByUserEmail(@Param("email") String email);

}