package com.example.productfinding.util;

import android.content.Intent;

import com.example.productfinding.model.Shop;
import com.example.productfinding.model.User;

public class IntentUtil {
    private static final String LOGIN_USER = "loginUser";
    private static final String CURRENT_SELECTED_SHOP = "currentSelectedShop";

    public static User getLoginUserFromIntent(Intent intent) {
        return (User) intent.getSerializableExtra(LOGIN_USER);
    }

    public static void setLoginUserForIntent(Intent intent, User user) {
        intent.putExtra(LOGIN_USER, user);
    }

    public static Shop getCurrentShopFromIntent(Intent intent) {
        return (Shop) intent.getSerializableExtra(CURRENT_SELECTED_SHOP);
    }

    public static void setCurrentSelectedShopForIntent(Intent intent, Shop shop) {
        intent.putExtra(CURRENT_SELECTED_SHOP, shop);
    }
}
