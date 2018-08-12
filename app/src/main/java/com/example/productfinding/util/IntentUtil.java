package com.example.productfinding.util;

import android.content.Intent;

import com.example.productfinding.model.User;

public class IntentUtil {

    public static User getLoginUserFromIntent(Intent intent) {
        return (User) intent.getSerializableExtra("loginUser");
    }

    public static void setLoginUserForIntent(Intent intent,User user){
        intent.putExtra("loginUser", user);
    }
}
