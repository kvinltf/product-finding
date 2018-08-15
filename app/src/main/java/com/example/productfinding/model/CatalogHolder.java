package com.example.productfinding.model;

import java.util.ArrayList;
import java.util.List;

public class CatalogHolder {
    private static List<Catalog> catalogListHolder = new ArrayList<>();

    public static List<Catalog> getCatalogListHolder() {
        return catalogListHolder;
    }

    public static void setCatalogListHolder(List<Catalog> catalogListHolder) {
        CatalogHolder.catalogListHolder = catalogListHolder;
    }

    public static void clearCatalogListHolder() {
        CatalogHolder.catalogListHolder.clear();
    }

    public static Boolean isEmpty() {
        return CatalogHolder.catalogListHolder.isEmpty();
    }

    public static int size() {
        return CatalogHolder.catalogListHolder.size();
    }

    public static void add(Catalog catalog) {
        CatalogHolder.catalogListHolder.add(catalog);
    }
}
