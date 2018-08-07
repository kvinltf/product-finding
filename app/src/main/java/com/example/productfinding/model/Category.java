package com.example.productfinding.model;

import java.io.Serializable;

public class Category implements Serializable {
    private int id;
    private String name;
    private Boolean isDeleted;
    private int parent_id;

    public Category(int id, String name, Boolean isDeleted, int parent_id) {
        this.id = id;
        this.name = name;
        this.isDeleted = isDeleted;
        this.parent_id = parent_id;
    }

    public Category(String name, Boolean isDeleted, int parent_id) {
        this.name = name;
        this.isDeleted = isDeleted;
        this.parent_id = parent_id;
    }

    public Category(String name, Boolean isDeleted) {
        this.name = name;
        this.isDeleted = isDeleted;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isDeleted=" + isDeleted +
                ", parent_id=" + parent_id +
                '}';
    }
}
