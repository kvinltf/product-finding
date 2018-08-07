package com.example.productfinding.model;

import java.io.Serializable;
import java.util.List;

class Item implements Serializable {
    private int id;
    private String name;
    private String description;
    private String brand;
    private Category category;
    private List<Shop> shopList;


    public Item(int id, String name, String description, String brand, Category category, List<Shop> shopList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.category = category;
        this.shopList = shopList;
    }

    public Item(int id, String name, String description, String brand, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.category = category;
    }

    public Item(String name, String description, String brand, Category category) {
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.category = category;
    }

    public Item(String name, String description, String brand) {
        this.name = name;
        this.description = description;
        this.brand = brand;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getBrand() {
        return brand;
    }

    public Category getCategory() {
        return category;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Shop> getShopList() {
        return shopList;
    }

    public void setShopList(List<Shop> shopList) {
        this.shopList = shopList;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", brand='" + brand + '\'' +
                ", category=" + category +
                ", shopList=" + shopList +
                '}';
    }
}
