package com.tp.todolist.dto;

public class UserJWT {
    private Long id;
    private String email;
    private String username;

    public UserJWT(Long id, String username, String email) {
        this.id = id;
        this.email = email;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
}
