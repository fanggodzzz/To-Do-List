package com.tp.todolist.dto;

public class UserJWT {
    private Long userId;
    private String userName;
    private String userEmail;
    private String userRole;

    public UserJWT(Long userId, String userName, String userEmail, String userRole) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userRole = userRole;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Long getId() {
        return userId;
    }

    public String getUsername() {
        return userName;
    }

    public String getEmail() {
        return userEmail;
    }

    public String getRole() {
        return userRole;
    }
}
