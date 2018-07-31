package com.example.productfinding.model;

import java.util.Date;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private java.util.Date created_on;

    public User(int id, String name, String email, String password, Date created_on) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.created_on = created_on;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreated_on() {
        return created_on;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", created_on=" + created_on +
                '}';
    }
}