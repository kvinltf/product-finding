package com.example.productfinding;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (item.getItemId()) {
            case R.id.bottom_navigation_login:
                LoginFragment loginFragment = new LoginFragment();
                fragmentManager.beginTransaction().replace(R.id.login_layout_content, loginFragment).commit();
                return true;
            case R.id.bottom_navigation_register:
                RegisterFragment registerFragment = new RegisterFragment();
                fragmentManager.beginTransaction().replace(R.id.login_layout_content, registerFragment).commit();
                return true;
            case R.id.bottom_navigation_forget_password:
                ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
                fragmentManager.beginTransaction().replace(R.id.login_layout_content, resetPasswordFragment).commit();
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.login_bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.bottom_navigation_login);
    }
}
