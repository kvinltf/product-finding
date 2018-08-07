package com.example.productfinding.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private int id;
    private String name;
    private String email;
    private String password;
    private String created_on;
    private List<Shop> shopList;

    public User(int id, String name, String email, String password, String created_on, List<Shop> shopList) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.created_on = created_on;
        this.shopList = shopList;
    }

    public User(int id, String name, String email, String password, String created_on) {
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

    public void setId(int id) {
        this.id = id;
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

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }


    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", created_on='" + created_on + '\'' +
                ", shopList=" + shopList +
                '}';
    }
}
