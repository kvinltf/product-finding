package com.example.productfinding.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

public class Shop implements Serializable {
    private int id;
    private String name;
    //    private LatLng latLng;
    private double latitude, longitude;
    private String description;
    private String created_on;
    private List<Item> itemList;

    public Shop() {
    }

    public Shop(int id, String name, double latitude, double longitude, String description, String created_on, List<Item> itemList) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.created_on = created_on;
        this.itemList = itemList;
    }

    public Shop(int id, String name, double latitude, double longitude, String description, String created_on) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.created_on = created_on;
    }

    public Shop(String name, double latitude, double longitude, String description) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LatLng getLatLng() {
        return new LatLng(this.latitude, this.longitude);
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", description='" + description + '\'' +
                ", created_on='" + created_on + '\'' +
                ", itemList=" + itemList +
                '}';
    }

}
