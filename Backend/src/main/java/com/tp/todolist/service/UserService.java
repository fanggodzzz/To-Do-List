package com.tp.todolist.service;

import org.springframework.stereotype.Service;

import com.tp.todolist.repository.UsersRepository;
import com.tp.todolist.entity.UsersEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersRepository userRepository;

    public UsersEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UsersEntity getUserByUsername(String username) {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UsersEntity getUserByEmail(String email) {
        return userRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UsersEntity createUser(UsersEntity user) {
        return userRepository.save(user);
    }
}
