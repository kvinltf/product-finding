package com.example.productfinding.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

public class Shop implements Serializable {
    private int id;
    private String name;
    private LatLng latLng;
    private String description;
    private String created_on;
    private List<Item> itemList;

    public Shop(int id, String name, LatLng latLng, String description, String created_on, List<Item> itemList) {
        this.id = id;
        this.name = name;
        this.latLng = latLng;
        this.description = description;
        this.created_on = created_on;
        this.itemList = itemList;
    }

    public Shop(String name, LatLng latLng, String description) {
        this.name = name;
        this.latLng = latLng;
        this.description = description;
    }

    public Shop(String name, double lat, double lng, String description) {
        this.name = name;
        this.latLng = new LatLng(lat, lng);
        this.description = description;
    }

    public Shop(int id, String name, LatLng latLng, String description, String created_on) {
        this.id = id;
        this.name = name;
        this.latLng = latLng;
        this.description = description;
        this.created_on = created_on;
    }

    public Shop(int id, String name, double lat, double lng, String description, String created_on) {
        this.id = id;
        this.name = name;
        this.latLng = new LatLng(lat, lng);
        this.description = description;
        this.created_on = created_on;
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

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(double lat, double lng) {
        this.latLng = new LatLng(lat, lng);
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", latLng=" + latLng +
                ", description='" + description + '\'' +
                ", created_on='" + created_on + '\'' +
                ", itemList=" + itemList +
                '}';
    }
}
