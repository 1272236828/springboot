package com.spring.web.entity;

public class User {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int isAdminAccount() {
        return adminAccount;
    }

    public void setAdminAccount(int adminAccount) {
        this.adminAccount = adminAccount;
    }

    private int id;
    private String username;
    private String password;
    private int adminAccount;
}
