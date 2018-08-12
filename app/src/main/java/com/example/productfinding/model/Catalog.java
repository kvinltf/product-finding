package com.example.productfinding.model;

import java.io.Serializable;

public class Catalog implements Serializable {
    private Shop shop;
    private Item item;

    public Catalog(Shop shop, Item item) {
        this.shop = shop;
        this.item = item;
    }

    public Catalog() {
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Shop getShop() {
        return shop;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "Catalog{" +
                "shop=" + shop +
                ", item=" + item +
                '}';
    }
}
