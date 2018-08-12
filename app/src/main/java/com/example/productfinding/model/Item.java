package com.example.productfinding.model;

import java.io.Serializable;
import java.util.List;


public class Item implements Serializable {
    private int id;
    private String name;
    private String description;
    private Brand brand;
    private Category category;
    private List<Shop> shopList;

    public Item() {
    }

    public Item(String name, String description, Brand brand, Category category) {
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.category = category;
    }

    public Item(int id, String name, String description, Brand brand, Category category, List<Shop> shopList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.category = category;
        this.shopList = shopList;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Category getCategory() {
        return category;
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
                ", brand=" + brand +
                ", category=" + category +
                ", shopList=" + shopList +
                '}';
    }
}
