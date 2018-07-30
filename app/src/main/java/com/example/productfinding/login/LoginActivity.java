package com.example.productfinding.login;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.productfinding.R;

public class LoginActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

        switch (item.getItemId()) {
            case R.id.bottom_navigation_login:
                replaceFragment(new LoginFragment());
                return true;
            case R.id.bottom_navigation_register:
                replaceFragment(new RegisterFragment());
                return true;
            case R.id.bottom_navigation_forget_password:
                replaceFragment(new ResetPasswordFragment());
                return true;
        }
        return false;
    };

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.login_layout_content, fragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.login_bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.bottom_navigation_login);
    }
}
